package com.example.mfaella.physicsapp;

import com.example.mfaella.physicsapp.actors.Actor;
import com.google.fpl.liquidfun.Body;

public interface CollisionHandler {
    void handleCollision(Actor otherActor, Body myBody, Body otherBody);
}
