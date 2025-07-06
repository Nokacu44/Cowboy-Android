package com.example.mfaella.physicsapp.actors.ui;

import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.components.ClickableComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PixmapManager;

public class Button extends Actor {

    private final SpriteComponent spriteComponent;

    public Button(GameLevel level, float x, float y, String texture, ClickableComponent.OnClickCallback onClick) {
        super(x, y);
        spriteComponent = addComponent(new SpriteComponent(PixmapManager.getPixmap(texture)));
        Log.d("BUTTON", "x: " + x + ", y: " + y + ", width: " + spriteComponent.frameWidth + ", height: " + spriteComponent.frameHeight);
        addComponent(new ClickableComponent(level.game.getInput(), new RectF(x, y, x + spriteComponent.frameWidth, y + spriteComponent.frameHeight), onClick));
    }
}
