package com.example.armando.game;

import com.example.armando.game.actors.Actor;
import com.google.fpl.liquidfun.Body;

public interface CollisionHandler {
    void handleCollision(Actor otherActor, Body myBody, Body otherBody);
}
