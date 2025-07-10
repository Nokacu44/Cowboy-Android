package com.example.mfaella.physicsapp.actors;

import android.util.Log;

import com.badlogic.androidgames.framework.Pool;
import com.example.mfaella.physicsapp.CollisionHandler;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.Deflection;
import com.example.mfaella.physicsapp.Tag;
import com.example.mfaella.physicsapp.events.GameEvents;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PhysicsManager;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;

public class Bullet extends Actor implements Pool.Poolable {
    private final PhysicsComponent physicsComponent;
    private Vec2 lastVelocity;

    public Bullet(GameLevel level) {
        super(level, -10, -10);
        addComponent(new SpriteComponent(PixmapManager.getPixmap("guns/bullet.png")));
        physicsComponent = addComponent(new PhysicsComponent(
                level,
                BodyType.dynamicBody,
                Coordinates.pixelsToMetersLengthsX(1),
                Coordinates.pixelsToMetersLengthsY(2),
                8f,
                this::handleCollision
        ));
        physicsComponent.body.setBullet(true);
        reset();
    }

    private void handleCollision(Actor otherActor, Body myBody, Body otherBody) {
        lastVelocity = myBody.getLinearVelocity();

        if (otherActor.hasTags(Tag.INVINCIBLE)) {
            return;
        }

        if (otherActor instanceof Hangman) {
            level.timerManager.scheduleOnce(this::reset, 5);
        }

        Log.d("VELOCITY", String.valueOf(myBody.getLinearVelocity().getX()));


        if (otherActor instanceof Deflection) {
            handleDeflectionCollision(myBody, (Deflection) otherActor);
        }
    }

    private void handleRopeSegmentCollision(Actor otherActor) {
        PhysicsComponent comp = otherActor.getComponent(PhysicsComponent.class);
        level.events.emit(GameEvents.EventType.ROPE_CUT);

        level.physicsManager.postTask(() -> {
            comp.clearJoints();
            comp.body.setActive(false);
            otherActor.getComponent(SpriteComponent.class).hide();
            reset();
        });
    }

    private void handleDeflectionCollision(Body myBody, Deflection deflection) {
        // Stop current velocity
        myBody.setLinearVelocity(new Vec2(0, 0));

        // Convert deflection angle to radians
        float angle = (float) Math.toRadians(deflection.getDeflectionAngle());

        // Calculate impulse vector using the exact angle
        float impulseX = (float) Math.cos(angle);
        float impulseY = (float) Math.sin(angle);

        // Apply impulse with a consistent magnitude
        float impulseStrength = 1.2f;
        Vec2 impulse = new Vec2(impulseX * impulseStrength, impulseY * impulseStrength);
        myBody.applyLinearImpulse(impulse, myBody.getWorldCenter(), true);

        // Set body rotation to match the exact deflection angle
        myBody.setTransform(myBody.getPosition(), angle);

        // Update class-level angle tracking if needed
        this.angle = angle;

        // Ensure the body maintains its orientation
        myBody.setFixedRotation(true);
    }

    public void shoot(float dirX, float dirY) {
        Vec2 impulse = new Vec2(dirX * 16 / 250f, dirY * 16 / 250f);
        physicsComponent.body.applyLinearImpulse(impulse, physicsComponent.body.getWorldCenter(), true);
        level.timerManager.scheduleOnce(this::reset, 3000);
    }

    @Override
    public void activate() {
        physicsComponent.body.setAwake(true);
        physicsComponent.body.setActive(true);
        getComponent(SpriteComponent.class).show();
    }

    @Override
    public void reset() {
        physicsComponent.body.setAwake(false);
        physicsComponent.body.setActive(false);
        getComponent(SpriteComponent.class).hide();
        physicsComponent.body.setTransform(0, 0, 0);
        x = 0;
        y = 0;
        angle = 0;
    }
}