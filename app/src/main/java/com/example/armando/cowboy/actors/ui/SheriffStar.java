package com.example.armando.cowboy.actors.ui;

import com.example.armando.cowboy.actors.Actor;
import com.example.armando.cowboy.components.SpriteComponent;
import com.example.armando.cowboy.levels.GameLevel;
import com.example.armando.cowboy.managers.PixmapManager;

public class SheriffStar extends Actor {
    public SheriffStar(GameLevel level, float x, float y, boolean gold) {
        super(level, x, y);
        addComponent(new SpriteComponent(PixmapManager.getPixmap("ui/sheriff_star_sheet.png"), 64, 64, 2));
        getComponent(SpriteComponent.class).setAnimating(false);
        getComponent(SpriteComponent.class).setCurrentFrame(gold ? 0 : 1);
    }

    public void makeGold() {
        getComponent(SpriteComponent.class).setCurrentFrame(0);
    }

    public void makeSilver() {
        getComponent(SpriteComponent.class).setCurrentFrame(1);

    }
}
