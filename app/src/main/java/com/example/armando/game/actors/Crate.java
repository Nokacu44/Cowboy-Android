package com.example.armando.game.actors;

import com.example.armando.game.Coordinates;
import com.example.armando.game.components.PhysicsComponent;
import com.example.armando.game.components.SpriteComponent;
import com.example.armando.game.levels.GameLevel;
import com.example.armando.game.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

public class Crate extends Actor {
    public Crate(GameLevel level, float x, float y) {
        super(level, x, y);
        addComponent(new PhysicsComponent(level, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(16f), Coordinates.pixelsToMetersLengthsY(16f), 0.5f));
        addComponent(new SpriteComponent(PixmapManager.getPixmap("environment/crate.png")));
    }

}
