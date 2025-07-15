package com.example.armando.game.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class ScorePersistence {

    private static final String PREF_NAME = "global_score_prefs";
    private static final String KEY_PREFIX = "level_";

    private final SharedPreferences preferences;

    public ScorePersistence(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveScore(String levelId, int stars) {
        preferences.edit().putInt(KEY_PREFIX + levelId, stars).apply();
    }

    public Map<String, Integer> loadAllScores() {
        Map<String, Integer> map = new HashMap<>();
        for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(KEY_PREFIX) && entry.getValue() instanceof Integer) {
                String levelId = key.substring(KEY_PREFIX.length());
                map.put(levelId, (Integer) entry.getValue());
            }
        }
        return map;
    }

    public void clearAll() {
        preferences.edit().clear().apply();
    }
}