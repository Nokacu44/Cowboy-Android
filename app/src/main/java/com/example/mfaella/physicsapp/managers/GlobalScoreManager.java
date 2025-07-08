package com.example.mfaella.physicsapp.managers;

import java.util.HashMap;

public class GlobalScoreManager {
    private final HashMap<String, Integer> levelStars = new HashMap<>();

    /**
     * Salva il punteggio solo se è maggiore del precedente
     */
    public void saveScore(String levelId, int stars) {
        int previous = levelStars.getOrDefault(levelId, 0);
        if (stars > previous) {
            levelStars.put(levelId, stars);
        }
    }

    public int getStarsForLevel(String levelId) {
        return levelStars.getOrDefault(levelId, 0);
    }

    public int getTotalStars() {
        int total = 0;
        for (int s : levelStars.values()) {
            total += s;
        }
        return total;
    }

    public HashMap<String, Integer> getAllScores() {
        return levelStars;
    }
}
