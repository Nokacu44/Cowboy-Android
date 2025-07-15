package com.example.armando.game.actors.ui;

import com.example.armando.game.Coordinates;
import com.example.armando.game.Deflection;
import com.example.armando.game.actors.Actor;
import com.example.armando.game.components.PhysicsComponent;
import com.example.armando.game.components.SpriteComponent;
import com.example.armando.game.events.GameEvents;
import com.example.armando.game.levels.GameLevel;
import com.example.armando.game.managers.PixmapManager;
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
