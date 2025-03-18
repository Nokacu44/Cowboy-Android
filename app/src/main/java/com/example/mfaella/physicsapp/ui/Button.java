package com.example.mfaella.physicsapp.ui;

import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.components.ClickableComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.managers.PixmapManager;

public class Button extends Actor {

    private final SpriteComponent spriteComponent;
    public Button(float x, float y, Input input) {
        super(x, y);
        spriteComponent = addComponent(new SpriteComponent(PixmapManager.getPixmap("ui/bang_btn.png")));

        addComponent(new ClickableComponent(input, new RectF(x, y, spriteComponent.w, spriteComponent.h), () -> Log.d("UI", "BUTTON PRESSED")));
    }
}
