package com.example.mfaella.physicsapp.levels;

import com.badlogic.androidgames.framework.Game;
import com.example.mfaella.physicsapp.actors.Actor;

@FunctionalInterface
public interface LevelFactory {
    GameLevel create(Game game);
}
