package com.example.mfaella.physicsapp.levels;

import static com.example.mfaella.physicsapp.events.GameEvents.EventType.GAME_FINISHED;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.example.mfaella.physicsapp.LevelLoader;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.actors.ui.LevelFailed;
import com.example.mfaella.physicsapp.actors.ui.LevelWin;
import com.example.mfaella.physicsapp.events.GameEvents;
import com.example.mfaella.physicsapp.managers.AudioManager;
import com.example.mfaella.physicsapp.managers.CinematicManager;
import com.example.mfaella.physicsapp.managers.GlobalScoreManager;
import com.example.mfaella.physicsapp.managers.PhysicsManager;
import com.example.mfaella.physicsapp.managers.TimerManager;
import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;

public abstract class GameLevel extends Screen {

    protected boolean active = true;
    protected boolean inputActive = true;

    public boolean finished = false;

    public final LevelRulesController levelRulesController;
    public final TimerManager timerManager = new TimerManager();
    public final PhysicsManager physicsManager = new PhysicsManager(new Vec2(0f, 16f));
    public final GameEvents events = new GameEvents();
    public final CinematicManager cinematicManager = new CinematicManager();

    ArrayList<Actor> actors = new ArrayList<>();



    protected void loadFromFile(String fileName) {
        actors = (ArrayList<Actor>) new LevelLoader(game.getFileIO()).loadActors(this, fileName);
        for (int i = 0; i < 60; i++) {
            physicsManager.update(1/60f);
        }
    }

    public GameLevel(Game game) {
        super(game);

        // Level Events
        levelRulesController = new LevelRulesController(events, timerManager, result -> {
            finished = true;
            events.emit(GAME_FINISHED);

            String currentLevelId = getLevelId();
            GlobalScoreManager.setStars(currentLevelId, result.stars);

            cinematicManager.applySlowMotion(0.6f);

            if (result.victory) {
                addActor(new LevelWin(this, result.stars));
            } else {
                addActor(new LevelFailed(this));
            }
        });


    }


    public void input(Input input) {
        if (!active || !inputActive) return;
    }

    @Override
    public void update(float deltaTime) {
        if (!active) return;
        deltaTime *= cinematicManager.slowMotionFactor;

        physicsManager.update(deltaTime);

        for (int i = 0; i < actors.size(); ++i) {
            actors.get(i).update(deltaTime);
        }

        input(game.getInput());
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clear(0xfaf5e1);

        for (int i = 0; i < actors.size(); ++i) {
            actors.get(i).draw(g);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    @Override
    public void dispose() {
        timerManager.cancelAll();
        physicsManager.dispose();
    }

    public abstract String getLevelId();
}
