package com.example.mfaella.physicsapp.managers;

import java.util.HashMap;
import java.util.Map;

public class GlobalScoreManager {

    private static final Map<String, Integer> levelStars = new HashMap<>();
    private static ScorePersistence persistenceManager;

    public static void init(ScorePersistence scorePersistence) {
        persistenceManager = scorePersistence;
        loadFrom(scorePersistence.loadAllScores());
    }

    public static void setStars(String levelId, int stars) {
        int previous = levelStars.getOrDefault(levelId, 0);
        if (stars > previous) {
            levelStars.put(levelId, stars);
            if (persistenceManager != null) {
                persistenceManager.saveScore(levelId, stars);
            }
        }
    }

    public static int getStars(String levelId) {
        return levelStars.getOrDefault(levelId, 0);
    }

    public static Map<String, Integer> getAllStars() {
        return new HashMap<>(levelStars);
    }

    public static int getTotalStars() {
        int total = 0;
        for (int stars : levelStars.values()) {
            total += stars;
        }
        return total;
    }

    public static void clear() {
        levelStars.clear();
    }

    public static void loadFrom(Map<String, Integer> data) {
        levelStars.clear();
        levelStars.putAll(data);
    }

    public static Map<String, Integer> export() {
        return new HashMap<>(levelStars);
    }
}
