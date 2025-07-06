package com.example.mfaella.physicsapp.actors;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.example.mfaella.physicsapp.components.SpriteComponent;

public class Player extends Actor{

    public final Gun gun;
    public Player(float x, float y, Input input) {
        super(x, y);
        name = "Player";
        addComponent(new SpriteComponent(PixmapManager.getPixmap("characters/cowboy.png")));
        gun = new Gun((float) x - 3, y - 2, input, 3);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        gun.update(dt);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        gun.draw(g);
    }
}
