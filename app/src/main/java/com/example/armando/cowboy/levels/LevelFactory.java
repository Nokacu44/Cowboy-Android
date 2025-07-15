package com.example.armando.cowboy.levels;

import com.badlogic.androidgames.framework.Game;

@FunctionalInterface
public interface LevelFactory {
    GameLevel create(Game game);
}
