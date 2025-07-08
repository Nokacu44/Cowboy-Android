package com.example.mfaella.physicsapp.actors;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.CollisionHandler;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PhysicsManager;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;

import java.util.List;

public class Bandit extends Actor {

    Actor alertActor;
    Bullet bullet;

    boolean shoot = false;

    PhysicsComponent physicsComponent;

    public Bandit(GameLevel level, float x, float y) {
        super(level, x, y);
        addComponent(new SpriteComponent(PixmapManager.getPixmap("characters/bandit_sheet.png"), 24, 19, 2));
        getComponent(SpriteComponent.class).setAnimating(false);

        CollisionHandler collisionHandler = (otherActor, myBody, otherBody) -> {
            if (otherActor instanceof Crate) {
                showDeathStatus();
                getComponent(SpriteComponent.class).setCurrentFrame(1);
                level.timerManager.scheduleOnce(() -> {
                    shoot();
                    alertActor.getComponent(SpriteComponent.class).hide();
                }, 500);
            }
            if (otherActor instanceof Bullet) {
                showDeathStatus();
                getComponent(SpriteComponent.class).setCurrentFrame(1);
                level.timerManager.scheduleOnce(() -> {
                    shoot = false;
                    alertActor.getComponent(SpriteComponent.class).hide();
                }, 500);
            }
        };
        physicsComponent = new PhysicsComponent(level, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(4f), Coordinates.pixelsToMetersLengthsX(19f), 5f, collisionHandler);
        addComponent(physicsComponent);

        bullet = new Bullet(level);
        bullet.x = x;
        bullet.y = y;
        alertActor = new Actor(level, x, y - 16, List.of(new SpriteComponent(PixmapManager.getPixmap("ui/status_icons.png"), 11, 12, 2)));
        alertActor.getComponent(SpriteComponent.class).hide();
        alertActor.getComponent(SpriteComponent.class).setAnimating(false);

    }


    public void showAlertStatus() {
        alertActor.getComponent(SpriteComponent.class).setCurrentFrame(0);
        alertActor.getComponent(SpriteComponent.class).show();
    }

    public void showDeathStatus() {
        alertActor.getComponent(SpriteComponent.class).setCurrentFrame(1);
        alertActor.getComponent(SpriteComponent.class).show();
    }

    public void shoot() {
        if (shoot) return;
        bullet.x = x;
        bullet.y = y;
        bullet.getComponent(PhysicsComponent.class).body.setTransform(new Vec2(Coordinates.toSimulationX(x + 4), Coordinates.toSimulationY(y)), (angle));
        float dirX = (float) Math.cos(angle) * 24;
        float dirY = (float) Math.sin(angle) * 24;
        bullet.activate();
        bullet.shoot(dirX , dirY );
        Log.d("BANDIT", "SHOOT");
        shoot = true;
        //0.0245f
        physicsComponent.body.applyLinearImpulse(new Vec2(-0.0245f, 0f), new Vec2(0, 0), true);

    }

    @Override
    public void update(float dt) {
        super.update(dt);
        level.physicsManager.physicsWorld.rayCast(new RayCastCallback() {
            @Override
            public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {

                Actor actor = (Actor) fixture.getBody().getUserData();
                if (shoot || !(actor instanceof Hangman)) return fraction;

                Log.d("BANDIT", (String.valueOf(actor)));

                //Entra in Alert
                showAlertStatus();

                // Poi spara
                level.timerManager.scheduleOnce(() -> {
                    shoot();
                    alertActor.getComponent(SpriteComponent.class).hide();
                }, 500);
                return 1;
            }
        }, Coordinates.toSimulationX(x), Coordinates.toSimulationY(y), Coordinates.toSimulationX(x + 400), Coordinates.toSimulationY(y));
        bullet.update(dt);
        alertActor.update(dt);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        alertActor.draw(g);
        bullet.draw(g);
    }
}
