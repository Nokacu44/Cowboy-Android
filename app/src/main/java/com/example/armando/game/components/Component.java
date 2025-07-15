package com.example.armando.game.components;

import com.badlogic.androidgames.framework.Graphics;
import com.example.armando.game.actors.Actor;
import com.example.armando.game.levels.GameLevel;

public abstract class Component {
    public GameLevel level;
    public Actor actor;
    public abstract void initialize(Actor actor) ;
    public abstract void update(float dt);
    public void draw(Graphics g) {}
}
