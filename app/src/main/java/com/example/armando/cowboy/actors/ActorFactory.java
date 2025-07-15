package com.example.armando.cowboy.actors;

import com.example.armando.cowboy.levels.GameLevel;

@FunctionalInterface
public interface ActorFactory {
    Actor create(GameLevel level, float x, float y);
}