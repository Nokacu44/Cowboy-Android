package com.example.armando.cowboy.levels;

public class LevelScoreTracker {
    private int shotsFired = 0;

    public void incrementShotsFired() {
        shotsFired++;
    }

    public int getShotsFired() {
        return shotsFired;
    }

    /**
     * - 1 colpo: 3 stelle
     * - 2 colpi: 2 stelle
     * - 3 colpi: 1 stella
     */
    public int calculateStars() {
        switch (shotsFired) {
            case 1: return 3;
            case 2: return 2;
            default: return 1;
        }
    }
}
