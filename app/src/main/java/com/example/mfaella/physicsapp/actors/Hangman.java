package com.example.mfaella.physicsapp.actors;

import static com.example.mfaella.physicsapp.events.GameEvents.EventType.HANGMAN_DEAD;

import android.util.Log;

import com.example.mfaella.physicsapp.CollisionHandler;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;

public class Hangman extends Actor {

    public Hangman(GameLevel level, float x, float y) {
        super(level, x, y);
        addComponent(new SpriteComponent(PixmapManager.getPixmap("characters/hangman_sheet.png"), 18, 25, 2));
        addComponent(new PhysicsComponent(level, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(8f), Coordinates.pixelsToMetersLengthsY(23f), 1.1f, this::onCollision));
        getComponent(SpriteComponent.class).setAnimating(false);
    }

    public void onCollision(Actor otherActor, Body myBody, Body otherBody) {
        if (otherActor instanceof Bullet) {
            Log.d("HANGMAN", "proiettile");
            death();
        } else if (otherActor instanceof Crate) {
            if (Coordinates.getVectorLength(otherBody.getLinearVelocity()) > 6) {
                death();
            }
        }
    }

    public void death() {
        getComponent(SpriteComponent.class).setCurrentFrame(1);
        level.events.emit(HANGMAN_DEAD);
    }
}
