package com.example.armando.game.managers;

public class CinematicManager {

    private static final float DEFAULT_SLOW_MOTION = 1f;

    public float slowMotionFactor = DEFAULT_SLOW_MOTION;

    public void resetSlowMotion() {
        slowMotionFactor = DEFAULT_SLOW_MOTION;
    }

    public void applySlowMotion(float slowMotion) {
        slowMotionFactor = slowMotion;
    }

}
