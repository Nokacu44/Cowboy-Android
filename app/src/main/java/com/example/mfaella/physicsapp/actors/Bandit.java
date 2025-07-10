package com.example.mfaella.physicsapp.actors;

import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.AudioManager;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;

import java.util.List;

public class Bandit extends Actor {
    private static final long ALERT_DELAY_MS = 500;
    private static final float BULLET_SPEED = 24f;
    private static final float RECOIL_IMPULSE = -0.0245f;
    private static final float RAY_CAST_DISTANCE = 400f;

    private Actor alertActor;
    private Bullet bullet;
    private PhysicsComponent physicsComponent;

    private RayCastCallback rayCastCallback;

    private boolean hasShot = false;
    private boolean isDead = false;

    private boolean facingRight;

    public Bandit(GameLevel level, float x, float y, boolean facingRight) {
        super(level, x, y);
        this.facingRight = facingRight;
        setupSprite();
        setupPhysics(level);
        setupBulletAndAlertActor(level, x, y);
    }

    private void setupSprite() {
        addComponent(new SpriteComponent(PixmapManager.getPixmap("characters/bandit_sheet.png"), 24, 19, 4));
        getComponent(SpriteComponent.class).setAnimating(false);
        getComponent(SpriteComponent.class).setCurrentFrame(facingRight ? 0 : 2);

    }

    private void setupPhysics(GameLevel level) {
        physicsComponent = new PhysicsComponent(
                level,
                BodyType.dynamicBody,
                Coordinates.pixelsToMetersLengthsX(4f),
                Coordinates.pixelsToMetersLengthsX(19f),
                5f,
                this::handleCollision
        );
        addComponent(physicsComponent);
        rayCastCallback = new RayCastCallback(){
            @Override
            public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
                return handleRayCastCallback(fixture, point, normal, fraction);
            }
        };
    }

    private void setupBulletAndAlertActor(GameLevel level, float x, float y) {
        bullet = new Bullet(level);
        bullet.x = x;
        bullet.y = y;

        alertActor = new Actor(level, x, y - 16, List.of(
                new SpriteComponent(PixmapManager.getPixmap("ui/status_icons.png"), 11, 12, 4)
        ));
        alertActor.getComponent(SpriteComponent.class).hide();
        alertActor.getComponent(SpriteComponent.class).setAnimating(false);
    }

    private void handleCollision(Actor otherActor, Body myBody, Body otherBody) {
        if (otherActor instanceof Crate) {
            handleDeathSequence(true);
        } else if (otherActor instanceof Bullet) {
            handleDeathSequence(false);
        }
    }

    private void handleDeathSequence(boolean shoot) {
        if (isDead) return;

        showDeathStatus();
        getComponent(SpriteComponent.class).setCurrentFrame(facingRight ? 1 : 3);

        level.timerManager.scheduleOnce(() -> {
            if (shoot) shoot();
            alertActor.getComponent(SpriteComponent.class).hide();
        }, ALERT_DELAY_MS);
    }

    public void showAlertStatus() {
        alertActor.getComponent(SpriteComponent.class).setCurrentFrame(0);
        alertActor.getComponent(SpriteComponent.class).show();
    }

    public void showDeathStatus() {
        if (isDead) return;

        alertActor.getComponent(SpriteComponent.class).setCurrentFrame(1);

        alertActor.getComponent(SpriteComponent.class).show();
        isDead = true;
        AudioManager.getSound("audio/wilhelmScream.mp3").play(100);
    }

    public void shoot() {
        if (hasShot) return;

        prepareBullet();
        fireBullet();
        applyRecoil();

        hasShot = true;
        AudioManager.getSound("audio/sparoPistola.mp3").play(100);
    }

    private void prepareBullet() {
        bullet.x = x;
        bullet.y = y;
        bullet.getComponent(PhysicsComponent.class).body.setTransform(
                new Vec2(Coordinates.toSimulationX(x + (facingRight ? 4 : -4)), Coordinates.toSimulationY(y)),
                facingRight ? angle : -angle
        );
    }

        private void fireBullet() {
            float dirX = (float) Math.cos(angle) * (facingRight ? BULLET_SPEED : -BULLET_SPEED);
            float dirY = (float) Math.sin(angle) *  (facingRight ? BULLET_SPEED : -BULLET_SPEED);

            bullet.activate();
            bullet.shoot(dirX, dirY);
            Log.d("BANDIT", "SHOOT");
        }

        private void applyRecoil() {
            physicsComponent.body.applyLinearImpulse(
                    new Vec2(facingRight ? RECOIL_IMPULSE : -RECOIL_IMPULSE, 0f),
                    new Vec2(0, 0),
                    true
            );
        }

        @Override
        public void update(float dt) {
            super.update(dt);

            if (isDead) return;

            level.physicsManager.physicsWorld.rayCast(rayCastCallback,
                    Coordinates.toSimulationX(x),
                    Coordinates.toSimulationY(y),
                    Coordinates.toSimulationX(x + (facingRight ? RAY_CAST_DISTANCE : -RAY_CAST_DISTANCE)),
                    Coordinates.toSimulationY(y)
            );

            bullet.update(dt);
            alertActor.update(dt);
        }


        private float handleRayCastCallback(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
            Actor actor = (Actor) fixture.getBody().getUserData();

            if (hasShot || !(actor instanceof Hangman)) {
                return fraction;
            }

            // Enter Alert state
            showAlertStatus();

            // Shoot after delay
            level.timerManager.scheduleOnce(() -> {
                shoot();
                alertActor.getComponent(SpriteComponent.class).hide();
            }, ALERT_DELAY_MS);

            return 1;
        }

        @Override
        public void draw(Graphics g) {
            super.draw(g);
            alertActor.draw(g);
            bullet.draw(g);
        }
    }