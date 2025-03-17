package com.example.mfaella.physicsapp.actors;

import android.util.Log;

import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.Managers.PixmapManager;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

public class Bullet extends Actor  {

    PhysicsComponent physicsComponent;

    public Bullet(float x, float y, World physicsWorld) {
        super(x, y);
        addComponent(new SpriteComponent(PixmapManager.getPixmap("guns/bullet.png")));
        physicsComponent = addComponent(new PhysicsComponent(physicsWorld, BodyType.kinematicBody, Coordinates.pixelsToMetersLengthsX(1), Coordinates.pixelsToMetersLengthsY(2)));
    }

    public void shoot(float dirX, float dirY) {
        physicsComponent.body.setLinearVelocity(new Vec2(dirX, dirY));
    }

}
