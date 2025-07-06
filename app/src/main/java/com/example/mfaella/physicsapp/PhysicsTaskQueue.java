package com.example.mfaella.physicsapp;

import java.util.Objects;
import java.util.Queue;
import java.util.LinkedList;

public class PhysicsTaskQueue {
    private final Queue<Runnable> queue = new LinkedList<>();

    /**
     * Accoda un'operazione da eseguire dopo world.step()
     */
    public void post(Runnable task) {
        synchronized (queue) {
            queue.add(task);
        }
    }

    /**
     * Esegue tutte le operazioni accodate.
     * Deve essere chiamato dopo world.step()
     */
    public void executeAll() {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                Objects.requireNonNull(queue.poll()).run();
            }
        }
    }
}