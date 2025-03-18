package com.example.mfaella.physicsapp.actors;

import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.example.mfaella.physicsapp.components.SpriteComponent;

public class Player extends Actor{

    public Player(float x, float y) {
        super(x, y);
        name = "Player";
        addComponent(new SpriteComponent(PixmapManager.getPixmap("characters/cowboy.png")));
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
