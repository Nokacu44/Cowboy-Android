package com.example.mfaella.physicsapp;

public class Camera {
    public float x = 0;
    public float y = 0;
    public float scale = 1.0f;

    private boolean zooming = false;
    private float startX, startY, startScale;
    private float targetX, targetY, targetScale;
    private float elapsed = 0;
    private float duration = 1.0f;

    /**
     * Avvia uno zoom verso un target con durata in secondi.
     */
    public void zoomToTarget(float targetX, float targetY, float targetScale, float durationSeconds) {
        this.startX = this.x;
        this.startY = this.y;
        this.startScale = this.scale;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetScale = targetScale;
        this.elapsed = 0;
        this.duration = durationSeconds;
        this.zooming = true;
    }

    public void update(float deltaTime) {
        if (!zooming) return;

        elapsed += deltaTime;
        float t = elapsed / duration;

        if (t >= 1.0f) {
            this.x = targetX;
            this.y = targetY;
            this.scale = targetScale;
            this.zooming = false;
        } else {
            // Easing (in-out cubic)
            float easedT = t * t * (3 - 2 * t);
            this.x = lerp(startX, targetX, easedT);
            this.y = lerp(startY, targetY, easedT);
            this.scale = lerp(startScale, targetScale, easedT);
        }
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
