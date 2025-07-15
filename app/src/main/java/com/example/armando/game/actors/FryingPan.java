package com.example.armando.game.actors;

import com.example.armando.game.Coordinates;
import com.example.armando.game.Deflection;
import com.example.armando.game.components.PhysicsComponent;
import com.example.armando.game.components.SpriteComponent;
import com.example.armando.game.levels.GameLevel;
import com.example.armando.game.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

public class FryingPan extends Actor implements Deflection {

    public float deflectionAngle;

    public FryingPan(GameLevel level, float x, float y, float deflectionAngle) {
        super(level, x, y);
        this.deflectionAngle = deflectionAngle;
        addComponent(new SpriteComponent(PixmapManager.getPixmap("environment/frying_pan.png")));
        addComponent(new PhysicsComponent(
                level,
                BodyType.dynamicBody,
                Coordinates.pixelsToMetersLengthsX(8f),
                Coordinates.pixelsToMetersLengthsY(17f),
                1f
        ));
    }

    public FryingPan(GameLevel level, float x, float y) {
        this(level, x, y, 0);
    }

    @Override
    public float getDeflectionAngle() {
        return deflectionAngle;
    }
}
