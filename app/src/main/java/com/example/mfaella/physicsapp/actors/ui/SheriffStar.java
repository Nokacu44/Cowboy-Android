package com.example.mfaella.physicsapp.actors.ui;

import android.graphics.RectF;

import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PixmapManager;

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
