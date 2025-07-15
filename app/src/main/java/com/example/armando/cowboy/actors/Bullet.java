package com.example.armando.cowboy.actors;

import com.example.armando.cowboy.Coordinates;
import com.example.armando.cowboy.Deflection;
import com.example.armando.cowboy.Tag;
import com.example.armando.cowboy.components.PhysicsComponent;
import com.example.armando.cowboy.components.SpriteComponent;
import com.example.armando.cowboy.events.GameEvents;
import com.example.armando.cowboy.levels.GameLevel;
import com.example.armando.cowboy.managers.PixmapManager;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Vec2;

public class Bullet extends Actor {
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

        //Log.d("VELOCITY", String.valueOf(myBody.getLinearVelocity().getX()));


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
        myBody.setLinearVelocity(new Vec2(0, 0));

        float angle = (float) Math.toRadians(deflection.getDeflectionAngle());

        float impulseX = (float) Math.cos(angle);
        float impulseY = (float) Math.sin(angle);

        float impulseStrength = 1.2f;
        Vec2 impulse = new Vec2(impulseX * impulseStrength, impulseY * impulseStrength);
        myBody.applyLinearImpulse(impulse, myBody.getWorldCenter(), true);

        myBody.setTransform(myBody.getPosition(), angle);

        this.angle = angle;

        myBody.setFixedRotation(true);
    }

    public void shoot(float dirX, float dirY) {
        Vec2 impulse = new Vec2(dirX * 16 / 250f, dirY * 16 / 250f);
        physicsComponent.body.applyLinearImpulse(impulse, physicsComponent.body.getWorldCenter(), true);
        level.timerManager.scheduleOnce(this::reset, 3000);
    }

    public void activate() {
        physicsComponent.body.setAwake(true);
        physicsComponent.body.setActive(true);
        getComponent(SpriteComponent.class).show();
    }

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