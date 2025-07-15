package com.example.armando.cowboy.actors;

import static com.example.armando.cowboy.events.GameEvents.EventType.HANGMAN_DEAD;

import com.example.armando.cowboy.Coordinates;
import com.example.armando.cowboy.components.PhysicsComponent;
import com.example.armando.cowboy.components.SpriteComponent;
import com.example.armando.cowboy.levels.GameLevel;
import com.example.armando.cowboy.managers.AudioManager;
import com.example.armando.cowboy.managers.PixmapManager;
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
        AudioManager.getSound("audio/wilhelmScream.mp3").play(100);
    }
}
