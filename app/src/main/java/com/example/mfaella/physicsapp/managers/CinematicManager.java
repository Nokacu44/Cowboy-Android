package com.example.mfaella.physicsapp.managers;

import com.example.mfaella.physicsapp.Camera;

public class CinematicManager {

    public static Camera camera = new Camera();

    private static final float DEFAULT_SLOW_MOTION = 1f;
    private static final float SHOOT_SLOW_MOTION = .2f;

    public static float slowMotionFactor = SHOOT_SLOW_MOTION; // TODO: creare un setter

    public static void resetSlowMotion() {
        slowMotionFactor = DEFAULT_SLOW_MOTION;
    }

    public static void shootSlowMotion() {slowMotionFactor = SHOOT_SLOW_MOTION; };

    public static void zoom_to_target(float x, float y, float duration) {

    }

}
