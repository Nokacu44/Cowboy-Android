package com.example.armando.cowboy.actors;

import com.example.armando.cowboy.Coordinates;
import com.example.armando.cowboy.components.PhysicsComponent;
import com.example.armando.cowboy.components.SpriteComponent;
import com.example.armando.cowboy.levels.GameLevel;
import com.example.armando.cowboy.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

public class Crate extends Actor {
    public Crate(GameLevel level, float x, float y) {
        super(level, x, y);
        addComponent(new PhysicsComponent(level, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(16f), Coordinates.pixelsToMetersLengthsY(16f), 0.5f));
        addComponent(new SpriteComponent(PixmapManager.getPixmap("environment/crate.png")));
    }

}
