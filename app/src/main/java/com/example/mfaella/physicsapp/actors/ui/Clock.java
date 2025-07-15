package com.example.mfaella.physicsapp.actors.ui;

import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.Deflection;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.events.GameEvents;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

public class Clock extends Actor implements Deflection {

    public Clock(GameLevel level, float x, float y) {
        super(level, x, y);
        SpriteComponent sprite = new SpriteComponent(PixmapManager.getPixmap("ui/clock_sheet.png"), 32, 32, 9);
        sprite.setFps(0.5f);
        sprite.setLooping(false);
        addComponent(sprite);
        PhysicsComponent physicsComponent = new PhysicsComponent(level, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(32), Coordinates.pixelsToMetersLengthsY(32), 0.2f);
        physicsComponent.body.setAngularDamping(8f);
        addComponent(physicsComponent);


        level.timerManager.scheduleOnce(() -> {
            level.events.emit(GameEvents.EventType.TIMEOUT);
        }, 18 * 1000);
    }

    @Override
    public float getDeflectionAngle() {
        return -30;
    }
}
