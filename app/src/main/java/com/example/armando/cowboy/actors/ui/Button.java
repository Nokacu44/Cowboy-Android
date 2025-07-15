package com.example.armando.cowboy.actors.ui;

import android.graphics.RectF;

import com.example.armando.cowboy.actors.Actor;
import com.example.armando.cowboy.components.ClickableComponent;
import com.example.armando.cowboy.components.SpriteComponent;
import com.example.armando.cowboy.levels.GameLevel;
import com.example.armando.cowboy.managers.PixmapManager;

public class Button extends Actor {

    public Button(GameLevel level, float x, float y, String texture, int frameWidth, int frameHeight, int frames, ClickableComponent.OnClickCallback onClick) {
        super(level, x, y);
        SpriteComponent spriteComponent = addComponent(new SpriteComponent(PixmapManager.getPixmap(texture), frameWidth, frameHeight, frames));
        //Log.d("BUTTON", "x: " + x + ", y: " + y + ", width: " + spriteComponent.frameWidth + ", height: " + spriteComponent.frameHeight);
        float halfWidth = (float) spriteComponent.frameWidth / 2;
        float halfHeight = (float) spriteComponent.frameHeight / 2;
        spriteComponent.setAnimating(false);
        addComponent(new ClickableComponent(level.game.getInput(), new RectF(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight), onClick));
    }
    public Button(GameLevel level, float x, float y, String texture, ClickableComponent.OnClickCallback onClick) {
        this(level, x, y, texture, PixmapManager.getPixmap(texture).getWidth(), PixmapManager.getPixmap(texture).getHeight(), 1, onClick);
    }
}
