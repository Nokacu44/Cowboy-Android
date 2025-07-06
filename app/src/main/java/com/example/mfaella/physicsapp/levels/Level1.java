package com.example.mfaella.physicsapp.levels;

import com.badlogic.androidgames.framework.Game;

public class Level1 extends GameLevel {

    public Level1(Game game) {
        super(game);
        loadFromFile("levels/cowboy/Level_0.ldtkl");
    }
}
