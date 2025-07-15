package com.example.armando.game.managers;

import com.badlogic.androidgames.framework.Game;
import com.example.armando.game.levels.GameLevel;
import com.example.armando.game.levels.LevelFactory;

public class LevelManager {
    private final Game game;
    private GameLevel currentLevel;
    private LevelFactory currentFactory;

    public LevelManager(Game game) {
        this.game = game;
    }

    public void startLevel(LevelFactory factory) {
        this.currentFactory = factory;
        this.currentLevel = factory.create(game);
        game.setScreen(currentLevel);
    }

    public void restartLevel() {
        if (currentFactory != null) {
            startLevel(currentFactory); // ricrea il livello daccapo
        }
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }
}