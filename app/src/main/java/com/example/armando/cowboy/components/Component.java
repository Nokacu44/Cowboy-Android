package com.example.armando.cowboy.components;

import com.badlogic.androidgames.framework.Graphics;
import com.example.armando.cowboy.actors.Actor;
import com.example.armando.cowboy.levels.GameLevel;

public abstract class Component {
    public GameLevel level;
    public Actor actor;
    public abstract void initialize(Actor actor) ;
    public abstract void update(float dt);
    public void draw(Graphics g) {}
}
