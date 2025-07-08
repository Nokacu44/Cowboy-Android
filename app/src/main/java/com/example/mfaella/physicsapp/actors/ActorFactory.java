package com.example.mfaella.physicsapp.actors;

import com.example.mfaella.physicsapp.levels.GameLevel;

@FunctionalInterface
public interface ActorFactory {
    Actor create(GameLevel level, float x, float y);
}