package com.example.mfaella.physicsapp.components;

import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.actors.Actor;

public class ClickableComponent extends Component {
    public interface OnClickCallback {
        void run();
    }

    private final RectF bounds;
    private final Input input;
    private final OnClickCallback onClickCallback;

    public ClickableComponent(Input input, RectF bounds, OnClickCallback onClickCallback) {
        this.input = input;
        this.onClickCallback = onClickCallback;
        this.bounds = bounds;
    }


    @Override
    public void initialize(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void update(float dt) {
        Log.d("UI", String.format("%s %s %s", bounds, input.getTouchX(0), input.getTouchY(0)));
        if (input.isTouchDown(0) && bounds.contains(input.getTouchX(0), input.getTouchY(0))) {
            if (onClickCallback != null) {
                onClickCallback.run();
            }
        }
    }
}
