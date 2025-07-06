package com.example.mfaella.physicsapp.actors;

import android.health.connect.datatypes.units.Mass;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.Tag;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.events.GameEventData;
import com.example.mfaella.physicsapp.events.GameEvents;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.MassData;
import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Rope extends Actor{
    public enum RopeType {
        SOFT,
        HARD
    }
    private final ArrayList<Actor> segments = new ArrayList<>();
    private final Actor endActor;

    public Rope(float x, float y, int numSegments, RopeType ropeType, BiFunction<Float, Float, Actor> endActorFactory) {
        super(x, y);

        // Primo segmento kinematic (ancorato in alto)
        PhysicsComponent physicsComponent = new PhysicsComponent(
                BodyType.kinematicBody,
                Coordinates.pixelsToMetersLengthsX(3f),
                Coordinates.pixelsToMetersLengthsY(4f)
        );
        addComponent(physicsComponent);
        addComponent(new SpriteComponent(PixmapManager.getPixmap(ropeType == RopeType.SOFT ?  "environment/rope_segment.png" : "environment/golden_chain.png") ));
        physicsComponent.clearJoints();

        PhysicsComponent previousComp = physicsComponent;
        float lastY = y;

        // Segmenti intermedi
        for (int i = 0; i < numSegments; i++) {
            PhysicsComponent comp = new PhysicsComponent(
                    BodyType.dynamicBody,
                    Coordinates.pixelsToMetersLengthsX(3f),
                    Coordinates.pixelsToMetersLengthsY(4f),
                    0.5f
            );
            comp.body.setAngularDamping(10f);
            float ay = lastY + (3 * i);

            Actor segment = new Actor(
                    x,
                    ay,
                    List.of(
                            comp,
                            addComponent(new SpriteComponent(PixmapManager.getPixmap(ropeType == RopeType.SOFT ?  "environment/rope_segment.png" : "environment/golden_chain.png") ))
                    )
            );
            segment.addTag(Tag.ROPE_SEGMENT);
            if (ropeType == RopeType.HARD) {
                segment.addTag(Tag.INVINCIBLE);
            }

            segments.add(segment);

            // Joint al precedente
            previousComp.addJoint(
                    comp,
                    new Vec2(0, comp.height / 2),
                    new Vec2(0, -previousComp.height / 1.2f),
                    true,
                    (float)Math.toRadians(-15),
                    (float)Math.toRadians(15)
            );

            previousComp = comp;
            lastY = ay;
        }

        // Actor finale (generico)
        endActor = endActorFactory.apply(x, lastY);
        PhysicsComponent endComp = endActor.getComponent(PhysicsComponent.class);
        if (endComp == null) {
            throw new IllegalArgumentException("L'actor finale deve avere un PhysicsComponent");
        }
        endComp.body.setAngularDamping(8f);
        endActor.y = lastY + Coordinates.metersToPixelsLengthY(endComp.height / 2);

        // Joint tra ultimo segmento e actor finale
        previousComp.addJoint(
                endComp,
                new Vec2(0, endComp.height / 2f),
                new Vec2(0, -previousComp.height / 2),
                true,
                (float)Math.toRadians(-15),
                (float)Math.toRadians(15)
        );

        // (esempio) Timer per scollegare l'actor dopo 5 secondi
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // endComp.clearJoints();
                    }
                },
                5000
        );
    }


    @Override
    public void update(float dt) {
        super.update(dt);
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).update(dt);
        }
        endActor.update(dt);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).draw(g);
        }
        endActor.draw(g);
    }
}
