package com.example.armando.game.components;

import android.graphics.RectF;

import com.badlogic.androidgames.framework.Input;
import com.example.armando.game.Coordinates;
import com.example.armando.game.actors.Actor;

public class ClickableComponent extends Component {
    public interface OnClickCallback {
        void run();
    }

    private final RectF bounds;
    private final Input input;
    private OnClickCallback onClickCallback;

    public ClickableComponent(Input input, RectF bounds, OnClickCallback onClickCallback) {
        this.input = input;
        this.onClickCallback = onClickCallback;
        this.bounds = bounds;
    }

    @Override
    public void initialize(Actor actor) {
        this.actor = actor;
    }

    public void setOnClickCallback(OnClickCallback callback) {
        this.onClickCallback = callback;
    }
    @Override
    public void update(float dt) {
        if (Coordinates.isInRect(input.getTouchX(0), input.getTouchY(0), bounds) && input.isTouchJustDown(0)  ) {
            if (onClickCallback != null) {
                onClickCallback.run();
            }
        }
    }
}
