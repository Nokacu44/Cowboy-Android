package com.example.mfaella.physicsapp.levels;

import android.util.Log;

import com.example.mfaella.physicsapp.events.GameEvents;
import com.example.mfaella.physicsapp.managers.TimerManager;

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

    private static final int CHECK_DELAY_MS = 1000;

    public LevelRulesController(GameEvents events, TimerManager timerManager, Consumer<LevelEndResult> onLevelEnd) {
        this.events = events;
        this.timerManager = timerManager;
        this.onLevelEnd = onLevelEnd;

        registerEventListeners();
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

        events.connect(GameEvents.EventType.OUT_OF_AMMO, data -> {
            outOfAmmo = true;
            evaluateState();
        });

        events.connect(GameEvents.EventType.SHOOT, data -> {
            scoreTracker.incrementShotsFired();
            evaluateState();
        });

        events.connect(GameEvents.EventType.TIMEOUT, data -> {
            timeout = true;
            evaluateState();
        });
    }

    private void evaluateState() {
        if (finished) return;

        if (timeout) {
            finishWithDefeat("Timeout");
            return;
        }

        if (hangmanDead) {
            finishWithDefeat("Hangman is dead");
            return;
        }

        if (outOfAmmo && !hangmanFreed) { // Se sprechi subito tutti i colpi senza nemmeno aspettare che sortiscano qualche effeto perdi...è voluto
            finishWithDefeat("Out of ammo and hangman not freed");
            return;
        }

        if (hangmanFreed) {
            scheduleVictoryCheck();
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
                // Se qualcosa è cambiato nel frattempo, rivaluta lo stato
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
        Log.d("DEFEAT: ", reason);
        onLevelEnd.accept(LevelEndResult.defeat());
    }

    public LevelScoreTracker getScoreTracker() {
        return scoreTracker;
    }

    public boolean isFinished() {
        return finished;
    }
}

