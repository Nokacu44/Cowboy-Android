package com.example.armando.cowboy;

import com.example.armando.cowboy.actors.Actor;
import com.google.fpl.liquidfun.Body;

public interface CollisionHandler {
    void handleCollision(Actor otherActor, Body myBody, Body otherBody);
}
