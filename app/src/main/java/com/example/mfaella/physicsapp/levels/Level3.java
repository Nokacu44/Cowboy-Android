package com.example.mfaella.physicsapp.levels;

import com.badlogic.androidgames.framework.Game;
import com.example.mfaella.physicsapp.actors.ui.MainUI;
import com.example.mfaella.physicsapp.managers.AudioManager;

public class Level3 extends GameLevel{

    public Level3(Game game) {
        super(game);
        loadFromFile("levels/cowboy/Level_3.ldtkl");
        actors.add(new MainUI(this));
        AudioManager.getSound("audio/level_intro.mp3").play(100);
        levelRulesController.setCheckDelaysMs(2000);
    }

    @Override
    public String getLevelId() {
        return "Level3";
    }
}
