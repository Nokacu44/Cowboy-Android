package com.example.mfaella.physicsapp.actors;

import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.World;

public class Crate extends Actor{
    public Crate(float x, float y, World physicsWorld) {
        super(x, y);
        addComponent(new PhysicsComponent(physicsWorld, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(16f), Coordinates.pixelsToMetersLengthsY(16f)));
        addComponent(new SpriteComponent(PixmapManager.getPixmap("environment/crate.png")));
    }
}
