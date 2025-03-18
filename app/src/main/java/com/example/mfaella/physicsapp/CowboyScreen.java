package com.example.mfaella.physicsapp;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.actors.Bullet;
import com.example.mfaella.physicsapp.actors.Crate;
import com.example.mfaella.physicsapp.actors.Gun;
import com.example.mfaella.physicsapp.actors.Player;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.ui.Button;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.List;

public class CowboyScreen extends Screen {

    private static final int VELOCITY_ITERATIONS = 2;
    private static final int POSITION_ITERATIONS = 6;
    private static final int PARTICLE_ITERATIONS = 4;

    private final World physicsWorld = new World(0, 16f);
    private final static ContactListener contactListener = new MyContactListener();

    private Gun gun;

    ArrayList<Actor> actors = new ArrayList<Actor>(){{
        add(new Crate((float) game.getGraphics().getWidth() / 2,  (float) game.getGraphics().getHeight() / 2, physicsWorld));
        add(new Crate((float) game.getGraphics().getWidth() / 2 - 6,  (float) game.getGraphics().getHeight() / 2 + 16, physicsWorld));
        add(new Actor((float) game.getGraphics().getWidth() / 2 + 8, game.getGraphics().getHeight() - 16f, List.of(
                new PhysicsComponent(physicsWorld, BodyType.staticBody, Coordinates.pixelsToMetersLengthsX(game.getGraphics().getWidth()), Coordinates.pixelsToMetersLengthsY(16f))
        )));

        add(new Actor(16, 16, List.of(
                new SpriteComponent(PixmapManager.getPixmap("environment/rock1.png"), 18, 9, 1),
                new PhysicsComponent(physicsWorld, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(18), Coordinates.pixelsToMetersLengthsY(7.5f))
        )));

        Player player = new Player((float) 64, game.getGraphics().getHeight() - 42f);
        add(player);

        List<Bullet> bullets = List.of( new Bullet(32, 32, physicsWorld));
        this.addAll(bullets);
        gun = new Gun((float) 61, player.y - 2, game.getInput(), physicsWorld, bullets);
        add(gun);

        // UI actor come gli altri? Si per lo scopo del gioco non cambia nulla
        add(new Button(32, 158, game.getInput()));
    }};


    public CowboyScreen(Game game) {
        super(game);
        // la GC dava problemi dopo un pò quindi lo salvo
        physicsWorld.setContactListener(contactListener);
    }

    @Override
    public void update(float deltaTime) {
        physicsWorld.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

        for (int i = 0; i < actors.size(); ++i) {
            actors.get(i).update(deltaTime);
        }

        Input inp = game.getInput();
        if (inp.isTouchDown(0)) {
            float x = (float) inp.getTouchX(0);
            float y = (float) inp.getTouchY(0);
            //Log.d("INPUT: ", String.format("Position %s,%s", x, y));
            gun.shoot();
        }
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

    @Override
    public void dispose() {

    }
}
