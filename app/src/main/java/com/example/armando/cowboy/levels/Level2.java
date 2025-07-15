package com.example.armando.cowboy.levels;

import com.badlogic.androidgames.framework.Game;
import com.example.armando.cowboy.actors.ui.MainUI;
import com.example.armando.cowboy.managers.AudioManager;

public class Level2 extends GameLevel{


    public Level2(Game game) {
        super(game);
        loadFromFile("levels/cowboy/Level_2.ldtkl");
        actors.add(new MainUI(this));
        AudioManager.getSound("audio/level_intro.mp3").play(100);
        levelRulesController.setCheckDelaysMs(2000);
    }

    @Override
    public String getLevelId() {
        return "Level2";
    }
}
