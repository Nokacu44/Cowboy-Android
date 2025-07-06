package com.example.mfaella.physicsapp.managers;

import com.example.mfaella.physicsapp.MyContactListener;
import com.example.mfaella.physicsapp.PhysicsTaskQueue;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class PhysicsManager {

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 6;
    private static final int PARTICLE_ITERATIONS = 4;

    public static final Vec2 GRAVITY = new Vec2(0f, 16f);
    public static final World physicsWorld = new World(GRAVITY.getX(), GRAVITY.getY());
    private static final PhysicsTaskQueue physicsQueue = new PhysicsTaskQueue();

    private final static ContactListener contactListener = new MyContactListener();

    static {
        physicsWorld.setContactListener(contactListener);
    }

    public static void updatePhysicsWorld(float deltaTime) {
        physicsWorld.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
    }

    public static void executeAllPhysicsTasks() {
        physicsQueue.executeAll();
    }

    public static void postTask(Runnable task) {
        physicsQueue.post(task);
    }
}
