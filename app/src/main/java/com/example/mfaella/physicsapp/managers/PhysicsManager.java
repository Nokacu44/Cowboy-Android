package com.example.mfaella.physicsapp.managers;

import com.example.mfaella.physicsapp.MyContactListener;
import com.example.mfaella.physicsapp.PhysicsTaskQueue;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class PhysicsManager {

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 6;
    private static final int PARTICLE_ITERATIONS = 4;

    public final World physicsWorld;
    private final PhysicsTaskQueue physicsQueue;
    private final ContactListener contactListener;

    private boolean destroyed = false;

    public Vec2 gravity;

    public PhysicsManager(Vec2 gravity) {
        this.gravity = gravity;
        this.physicsWorld = new World(gravity.getX(), gravity.getY());
        this.physicsQueue = new PhysicsTaskQueue();
        this.contactListener = new MyContactListener();
        this.physicsWorld.setContactListener(contactListener);
    }

    public void updatePhysicsWorld(float deltaTime) {
        if (destroyed) {
            return;
        }
        physicsWorld.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
    }

    public void executeAllPhysicsTasks() {
        if (destroyed) return;
        physicsQueue.executeAll();
    }

    public void postTask(Runnable task) {
        if (destroyed) return;
        physicsQueue.post(task);
    }


    public void dispose() {
        if (destroyed) return;
        physicsWorld.delete();
        // Eventuale cleanup di altre risorse (se serve)
        // Ad esempio: particle systems, se li usi
        destroyed = true;
    }
}
