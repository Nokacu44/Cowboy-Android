package com.example.armando.game.levels;

import com.badlogic.androidgames.framework.Game;
import com.example.armando.game.actors.ui.MainUI;
import com.example.armando.game.managers.AudioManager;

public class Level1 extends GameLevel {

    public Level1(Game game) {
        super(game);
        loadFromFile("levels/cowboy/Level_1.ldtkl");
        actors.add(new MainUI(this));
        AudioManager.getSound("audio/level_intro.mp3").play(100);
        levelRulesController.setCheckDelaysMs(2000);

    }

    @Override
    public String getLevelId() {
        return "Level1";
    }
}
