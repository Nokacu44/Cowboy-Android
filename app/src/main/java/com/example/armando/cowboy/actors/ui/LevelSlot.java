package com.example.armando.cowboy.actors.ui;

import com.badlogic.androidgames.framework.Graphics;
import com.example.armando.cowboy.actors.Actor;
import com.example.armando.cowboy.components.ClickableComponent;
import com.example.armando.cowboy.components.SpriteComponent;
import com.example.armando.cowboy.levels.GameLevel;
import com.example.armando.cowboy.managers.PixmapManager;

import java.util.List;

public class LevelSlot extends Button {

    Actor starLabel;
    public LevelSlot(GameLevel level, float x, float y, String texture, ClickableComponent.OnClickCallback onClick, int stars) {
        super(level, x, y, texture, onClick);
        starLabel = new Actor(
                level, x, y + 32,
                List.of(new SpriteComponent(PixmapManager.getPixmap("ui/star_level_label.png"), 120, 58, 4)));
        starLabel.getComponent(SpriteComponent.class).setAnimating(false);
        starLabel.getComponent(SpriteComponent.class).setCurrentFrame(stars);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        starLabel.draw(g);
    }
}
