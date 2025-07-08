package com.example.mfaella.physicsapp.levels;

public class LevelEndResult {
    public final boolean victory;
    public final int stars;

    private LevelEndResult(boolean victory, int stars) {
        this.victory = victory;
        this.stars = stars;
    }

    public static LevelEndResult victory(int stars) {
        return new LevelEndResult(true, stars);
    }

    public static LevelEndResult defeat() {
        return new LevelEndResult(false, 0);
    }
}