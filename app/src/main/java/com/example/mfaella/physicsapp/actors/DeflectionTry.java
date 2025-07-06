package com.example.mfaella.physicsapp.actors;

import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.Deflection;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

public class DeflectionTry extends Actor implements Deflection {

    public DeflectionTry(float x, float y) {
        super(x, y);
        addComponent(new PhysicsComponent(BodyType.staticBody, Coordinates.pixelsToMetersLengthsX(16f), Coordinates.pixelsToMetersLengthsY(16f)));
        addComponent(new SpriteComponent(PixmapManager.getPixmap("environment/crate.png")));
    }

    @Override
    public float getDeflectionAngle() {
        return -45;
    }
}
