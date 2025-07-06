package com.example.mfaella.physicsapp.actors;

import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

public class Hangman extends Actor {

    public Hangman(float x, float y) {
        super(x, y);
        addComponent(new SpriteComponent(PixmapManager.getPixmap("characters/hangman1.png")));
        addComponent(new PhysicsComponent(BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(8f), Coordinates.pixelsToMetersLengthsY(23f), 1.1f));
    }
}
