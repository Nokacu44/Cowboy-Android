package com.example.mfaella.physicsapp.actors;

import android.os.Handler;
import android.os.Looper;

public class GameTimer {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable action;
    private final long intervalMillis;
    private final boolean repeating;

    private boolean running = false;

    private final Runnable internalRunnable = new Runnable() {
        @Override
        public void run() {
            if (!running) return;
            action.run();
            if (repeating) {
                handler.postDelayed(this, intervalMillis);
            } else {
                running = false;
            }
        }
    };

    public GameTimer(Runnable action, long delayMillis, boolean repeating) {
        this.action = action;
        this.intervalMillis = delayMillis;
        this.repeating = repeating;
    }

    public void start() {
        if (running) return;
        running = true;
        handler.postDelayed(internalRunnable, intervalMillis);
    }

    public void cancel() {
        running = false;
        handler.removeCallbacks(internalRunnable);
    }

    public boolean isRunning() {
        return running;
    }
}
