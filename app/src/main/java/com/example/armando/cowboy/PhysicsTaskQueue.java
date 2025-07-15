package com.example.armando.cowboy;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

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