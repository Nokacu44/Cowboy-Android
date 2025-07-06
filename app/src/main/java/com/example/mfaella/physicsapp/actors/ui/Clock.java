package com.example.mfaella.physicsapp.actors.ui;

import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

public class Clock extends Actor {

    public Clock(float x, float y) {
        super(x, y);
        SpriteComponent sprite = new SpriteComponent(PixmapManager.getPixmap("ui/clock_sheet.png"), 32, 32, 9);
        sprite.setFps(0.5f);
        addComponent(sprite);
        PhysicsComponent physicsComponent = new PhysicsComponent(BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(32), Coordinates.pixelsToMetersLengthsY(32), 0.2f);
        physicsComponent.body.setAngularDamping(8f);
        addComponent(physicsComponent);
    }

}
