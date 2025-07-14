package com.example.mfaella.physicsapp;

import java.util.Objects;
import java.util.Queue;
import java.util.LinkedList;

public class PhysicsTaskQueue {
    private final Queue<Runnable> queue = new LinkedList<>();

    public void post(Runnable task) {
        synchronized (queue) {
            queue.add(task);
        }
    }

    public void executeAll() {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                Objects.requireNonNull(queue.poll()).run();
            }
        }
    }
}