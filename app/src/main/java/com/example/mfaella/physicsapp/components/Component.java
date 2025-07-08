package com.example.mfaella.physicsapp.components;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.levels.GameLevel;

public abstract class Component {
    public GameLevel level;
    public Actor actor;
    public abstract void initialize(Actor actor) ;
    public abstract void update(float dt);
    public void draw(Graphics g) {}
}
