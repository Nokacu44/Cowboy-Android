package com.example.armando.game.actors;

import static com.example.armando.game.events.GameEvents.EventType.BANG_BUTTON_PRESSED;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.example.armando.game.components.SpriteComponent;
import com.example.armando.game.levels.GameLevel;
import com.example.armando.game.managers.PixmapManager;

public class Player extends Actor{

    public final Gun gun;
    public Player(GameLevel level, float x, float y, Input input) {
        super(level, x, y);
        name = "Player";
        addComponent(new SpriteComponent(PixmapManager.getPixmap("characters/cowboy.png")));
        gun = new Gun(level, (float) x - 3, y - 2, input, 3);
        level.events.connect(BANG_BUTTON_PRESSED, (data) -> gun.shoot());

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
