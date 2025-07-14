package com.example.mfaella.physicsapp.managers;

public class CinematicManager {

    private static final float DEFAULT_SLOW_MOTION = 1f;
    private static final float SHOOT_SLOW_MOTION = .2f;

    public static float slowMotionFactor = DEFAULT_SLOW_MOTION;

    public static void resetSlowMotion() {
        slowMotionFactor = DEFAULT_SLOW_MOTION;
    }


}
