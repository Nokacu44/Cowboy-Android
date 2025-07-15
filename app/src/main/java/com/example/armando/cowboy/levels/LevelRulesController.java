package com.example.armando.cowboy.levels;

import com.example.armando.cowboy.events.GameEvents;
import com.example.armando.cowboy.managers.TimerManager;

import java.util.function.Consumer;

public class LevelRulesController {

    private final TimerManager timerManager;
    private final GameEvents events;
    private final Consumer<LevelEndResult> onLevelEnd;

    private final LevelScoreTracker scoreTracker = new LevelScoreTracker();

    private boolean hangmanFreed = false;
    private boolean hangmanDead = false;
    private boolean outOfAmmo = false;
    private boolean timeout = false;

    private boolean finished = false;
    private boolean victoryCheckScheduled = false;
    private boolean defeatCheckScheduled = false;

    private int CHECK_DELAY_MS = 1000;

    public LevelRulesController(GameEvents events, TimerManager timerManager, Consumer<LevelEndResult> onLevelEnd) {
        this.events = events;
        this.timerManager = timerManager;
        this.onLevelEnd = onLevelEnd;

        registerEventListeners();
    }


    public void setCheckDelaysMs(int ms) {
        CHECK_DELAY_MS = ms;
    }
    private void registerEventListeners() {
        events.connect(GameEvents.EventType.ROPE_CUT, data -> {
            hangmanFreed = true;
            evaluateState();
        });

        events.connect(GameEvents.EventType.HANGMAN_DEAD, data -> {
            hangmanDead = true;
            evaluateState();
        });

        events.connect(GameEvents.EventType.SHOOT, data -> {
            scoreTracker.incrementShotsFired();
            evaluateState();
        });

        events.connect(GameEvents.EventType.OUT_OF_AMMO, data -> {
            outOfAmmo = true;

            // Ritarda la valutazione per permettere ad altri eventi (es. ROPE_CUT) di arrivare
            if (!defeatCheckScheduled) {
                defeatCheckScheduled = true;
                timerManager.scheduleOnce(this::evaluateState, CHECK_DELAY_MS);
            }
        });

        events.connect(GameEvents.EventType.TIMEOUT, data -> {
            timeout = true;
            evaluateState();
        });
    }

    private void evaluateState() {
        if (finished) return;

        // Timeout
        if (timeout) {
            finishWithDefeat("Timeout");
            return;
        }

        // Sconfitta se hangman è morto
        if (hangmanDead) {
            finishWithDefeat("Hangman is dead");
            return;
        }

        // Vittoria se è stato liberato e non è morto (con check ritardato per sicurezza)
        if (hangmanFreed && !hangmanDead) {
            scheduleVictoryCheck();
            return;
        }

        // Sconfitta se ho finito i colpi e non è stato liberato
        if (outOfAmmo && !hangmanFreed) {
            finishWithDefeat("Out of ammo and hangman not freed");
        }
    }

    private void scheduleVictoryCheck() {
        if (victoryCheckScheduled) return;

        victoryCheckScheduled = true;

        timerManager.scheduleOnce(() -> {
            if (finished) return;

            if (hangmanFreed && !hangmanDead) {
                finishWithVictory();
            } else {
                // Potrebbe essere morto dopo la liberazione: rianalizza
                victoryCheckScheduled = false;
                evaluateState();
            }
        }, CHECK_DELAY_MS);
    }

    private void finishWithVictory() {
        finished = true;
        onLevelEnd.accept(LevelEndResult.victory(scoreTracker.calculateStars()));
    }

    private void finishWithDefeat(String reason) {
        finished = true;
        System.out.println("Defeat: " + reason); // utile per debugging
        onLevelEnd.accept(LevelEndResult.defeat());
    }

    public LevelScoreTracker getScoreTracker() {
        return scoreTracker;
    }

    public boolean isFinished() {
        return finished;
    }
}

