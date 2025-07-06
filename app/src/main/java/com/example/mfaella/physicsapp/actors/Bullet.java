package com.example.mfaella.physicsapp.actors;

import android.util.Log;

import com.badlogic.androidgames.framework.Pool;
import com.example.mfaella.physicsapp.CollisionHandler;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.Deflection;
import com.example.mfaella.physicsapp.Tag;
import com.example.mfaella.physicsapp.events.GameEvents;
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
    private final RayCastCallback rayCastCallback;

    private Vec2 lastVelocity;

    public Bullet() {
        super(-10, -10);
        addComponent(new SpriteComponent(PixmapManager.getPixmap("guns/bullet.png")));
        physicsComponent = addComponent(new PhysicsComponent(BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(1), Coordinates.pixelsToMetersLengthsY(2), 8f,
                (otherActor, myBody, otherBody) -> {
                    lastVelocity = myBody.getLinearVelocity();

                    Log.d("VELOCITY", String.valueOf(myBody.getLinearVelocity().getX()));
                    if (otherActor.hasTags(Tag.ROPE_SEGMENT) && !otherActor.hasTags(Tag.INVINCIBLE)) {
                        PhysicsComponent comp = otherActor.getComponent(PhysicsComponent.class);
                        GameEvents.emit(GameEvents.EventType.ROPE_CUT);

                        PhysicsManager.postTask(() -> {
                            comp.clearJoints();
                            comp.body.setActive(false);
                            otherActor.getComponent(SpriteComponent.class).hide();
                            this.removeComponent(SpriteComponent.class);
                        });
                    } else if (otherActor instanceof Deflection) {
                        myBody.setLinearVelocity(new Vec2(0, 0));

                        float angle = (float) Math.toRadians(((Deflection) otherActor).getDeflectionAngle());  // 45 gradi -> 45 * (pi / 180)

                        float impulseX = (float) Math.cos(angle);  // Componente x dell'impulso
                        float impulseY = (float) Math.sin(angle);  // Componente y dell'impulso

                        Vec2 impulse = new Vec2(impulseX * 1.2f, impulseY * 1.2f);

                        myBody.applyLinearImpulse(impulse, myBody.getWorldCenter(), true);

                        float rotationAngle = (float) Math.atan2(impulseY, impulseX);

                        myBody.setTransform(myBody.getPosition(), rotationAngle);
                        this.angle = (float)(rotationAngle);

                        myBody.setFixedRotation(true);
                    }
                }
        ));
        physicsComponent.body.setBullet(true);
        rayCastCallback = new RayCastCallback() {
            @Override
            public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
                Actor actor = (Actor) fixture.getBody().getUserData();
                Log.d("RAYCAST", (String.valueOf(actor)));
                return 1;
            }
        };
        reset();
    }

    public void shoot(float dirX, float dirY) {
        physicsComponent.body.applyLinearImpulse(new Vec2((dirX * 16 / 250 ) , (dirY * 16 / 250) ), physicsComponent.body.getWorldCenter(), true);
        Log.d("VELOCITY", String.valueOf(physicsComponent.body.getLinearVelocity().getX()));

    }

    @Override
    public void update(float dt) {
        super.update(dt);
        //Log.d("BULLET", String.valueOf(x));
        Vec2 center = physicsComponent.body.getWorldCenter();
        float x2 = x + 8f * (float) Math.cos(this.angle);
        float y2 = y + 8f * (float) Math.sin(this.angle);
        PhysicsManager.physicsWorld.rayCast(rayCastCallback,  center.getX(), center.getY(), Coordinates.toSimulationX(x2), Coordinates.toSimulationY(y2));
    }

    @Override
    public void activate() {
        physicsComponent.body.setAwake(true);
        physicsComponent.body.setActive(true);

    }

    @Override
    public void reset() {
        physicsComponent.body.setAwake(false);
        physicsComponent.body.setActive(false);
        physicsComponent.body.setTransform(0, 0, 0);
        x = 0;
        y = 0;
        angle = 0;
    }
}
