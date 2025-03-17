package com.example.mfaella.physicsapp.components;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.actors.Actor;

public abstract class Component {
    public Actor actor;
    public abstract void initialize(Actor actor) ;
    public abstract void update(float dt);
    public void draw(Graphics g) {}
}
