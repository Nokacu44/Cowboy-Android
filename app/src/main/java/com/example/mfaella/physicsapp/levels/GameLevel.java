package com.example.mfaella.physicsapp.levels;

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
//    public final CinematicManager cinematicManager = new CinematicManager();
//    public final AudioManager audioManager = new AudioManager();

    ArrayList<Actor> actors = new ArrayList<>();

    protected void loadFromFile(String fileName) {
        actors = (ArrayList<Actor>) new LevelLoader(game.getFileIO()).loadActors(this, fileName);
        for (int i = 0; i < 60; i++) {
            physicsManager.updatePhysicsWorld(1/60f);
        }
    }

    public GameLevel(Game game) {
        super(game);

        // la GC dava problemi dopo un pò quindi lo salvo
        // TODO: inserire nel physics manager
        //PhysicsManager.physicsWorld.setContactListener(contactListener);

        // Game Events
        CinematicManager.resetSlowMotion();
        //GameEvents.connect(GameEvents.EventType.SHOOT, (data) -> CinematicManager.shootSlowMotion());

        // Level Events
        levelRulesController = new LevelRulesController(events, timerManager, result -> {
            finished = true;

            String currentLevelId = getLevelId();
            GlobalScoreManager.setStars(currentLevelId, result.stars);

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
//        CinematicManager.camera.update(deltaTime);

//        AndroidGraphics graphics = (AndroidGraphics) game.getGraphics();
//        Camera camera = CinematicManager.camera;
//
//        graphics.canvas.scale(camera.scale, camera.scale);
//        graphics.canvas.translate(-camera.x * 2, -camera.y * 2.8f);

        deltaTime = deltaTime * CinematicManager.slowMotionFactor;
        deltaTime = Math.min(deltaTime, 1/30f);
//        Vec2 acturalGravity = gravity;
//        acturalGravity.setX(acturalGravity.getX() * (CinematicManager.slowMotionFactor));  // Applica il fattore di slow-motion alla gravità
//        acturalGravity.setY(acturalGravity.getY() * (CinematicManager.slowMotionFactor));  // Applica il fattore di slow-motion alla gravità
//
//        physicsWorld.setGravity(acturalGravity.getX(), acturalGravity.getY());

        physicsManager.updatePhysicsWorld(deltaTime);
        physicsManager.executeAllPhysicsTasks();

        for (int i = 0; i < actors.size(); ++i) {
            actors.get(i).update(deltaTime);
        }

        input(game.getInput());

//        if (inp.isTouchDown(0)) {
//            float x = (float) inp.getTouchX(0);
//            float y = (float) inp.getTouchY(0);
//            Log.d("INPUT: ", String.format("Position %s,%s", x, y));
//        }
//        if (inp.isTouchJustRelease(0)) {
//            Log.d("INPUT ", "JUST RELEASE");
//        }
//        if (inp.isTouchJustDown(0)) {
//            Log.d("INPUT ", "JUST DOWN");
//        }
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
