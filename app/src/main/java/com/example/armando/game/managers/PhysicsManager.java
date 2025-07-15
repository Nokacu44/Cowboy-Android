package com.example.armando.game.managers;

import com.example.armando.game.MyContactListener;
import com.example.armando.game.PhysicsTaskQueue;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;


public class PhysicsManager {

    private static final float FIXED_TIME_STEP = 1f / 60f;
    private static final float MAX_ACCUMULATED_TIME = 0.25f;
    private float accumulator = 0f;

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

    public void update(float deltaTime) {
        if (destroyed) return;

        deltaTime = Math.min(deltaTime, MAX_ACCUMULATED_TIME);

        accumulator += deltaTime;

        while (accumulator >= FIXED_TIME_STEP) {
            physicsWorld.step(FIXED_TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
            physicsQueue.executeAll();
            accumulator -= FIXED_TIME_STEP;
        }
    }

    public void postTask(Runnable task) {
        if (destroyed) return;
        physicsQueue.post(task);
    }


    public void dispose() {
        if (destroyed) return;
        physicsWorld.delete();
        destroyed = true;
    }
}
