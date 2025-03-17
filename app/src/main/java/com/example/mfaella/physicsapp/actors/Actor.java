package com.example.mfaella.physicsapp.actors;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.components.Component;

import java.util.List;

public class Actor {

    public float x, y, angle;
    public String name;

    private final ArrayMap<Class<? extends Component>, Component> components = new ArrayMap<>();

    public Actor(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Actor(float x, float y, List<Component> components) {
        this.x = x;
        this.y = y;
        for (Component component : components) {
            addComponent(component);
        }
    }

    public void update(float dt) {
        for (Component comp : components.values()) {
            comp.update(dt);
        }
    }

    public void draw(Graphics g) {
        for (Component comp : components.values()) {
            comp.draw(g);
        }
    }

    public <T extends Component> T addComponent(T component)  {
        components.put(component.getClass(), component);
        component.initialize(this);
        component.actor = this;
        return component;
    }

    public <T extends Component> void removeComponent(@NonNull Class<T> componentClass) {
        components.remove(componentClass);
    }
    public <T extends Component> T getComponent(@NonNull Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }
    public <T extends Component> boolean hasComponent(@NonNull Class<T> componentClass) {
        return components.containsKey(componentClass);
    }

}
