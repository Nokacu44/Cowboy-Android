package com.example.armando.game.actors;

import com.example.armando.game.levels.GameLevel;

@FunctionalInterface
public interface ActorFactory {
    Actor create(GameLevel level, float x, float y);
}