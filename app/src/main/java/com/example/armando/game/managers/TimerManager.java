package com.example.armando.game.managers;

import com.example.armando.game.actors.GameTimer;

import java.util.ArrayList;

public class TimerManager {
    private final ArrayList<GameTimer> timers = new ArrayList<>();

    public GameTimer scheduleOnce(Runnable action, long delayMillis) {
        GameTimer timer = new GameTimer(action, delayMillis, false);
        timers.add(timer);
        timer.start();
        return timer;
    }

    public GameTimer scheduleRepeating(Runnable action, long intervalMillis) {
        GameTimer timer = new GameTimer(action, intervalMillis, true);
        timers.add(timer);
        timer.start();
        return timer;
    }

    public void cancelAll() {
        for (GameTimer t : timers) {
            t.cancel();
        }
        timers.clear();
    }
}