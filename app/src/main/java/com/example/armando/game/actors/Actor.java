package com.example.armando.game.actors;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.badlogic.androidgames.framework.Graphics;
import com.example.armando.game.Tag;
import com.example.armando.game.components.Component;
import com.example.armando.game.levels.GameLevel;

import java.util.List;

public class Actor {

    public GameLevel level;
    public float x, y, angle;
    public String name;

    private int tags = 0;

    private final ArrayMap<Class<? extends Component>, Component> components = new ArrayMap<>();

    public Actor(GameLevel level, float x, float y) {
        this(level, x, y, List.of());
    }

    public Actor(GameLevel level, float x, float y, List<Component> components) {
        this.level = level;
        this.x = x;
        this.y = y;
        for (Component component : components) {
            addComponent(component);
        }
        angle = 0;
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
        component.level = this.level;
        return component;
    }

    public <T extends Component> T removeComponent(@NonNull Class<T> componentClass) {
        return componentClass.cast(components.remove(componentClass));

    }
    public <T extends Component> T getComponent(@NonNull Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }
    public <T extends Component> boolean hasComponent(@NonNull Class<T> componentClass) {
        return components.containsKey(componentClass);
    }

    public void addTag(Tag tag) {
        tags |= tag.getMask();
    }

    public void removeTag(Tag tag) {
        tags &= ~tag.getMask();
    }

    public boolean hasTags(Tag ...tags) {
        int mask = 0;
        for (Tag tag : tags) {
            mask |= tag.getMask();
        }
        return (this.tags & mask) == mask;
    }

    public void clearTags() {
        tags = 0;
    }

}
