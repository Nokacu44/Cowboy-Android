package com.badlogic.androidgames.framework.impl;

import java.util.List;

import android.view.View.OnTouchListener;

import com.badlogic.androidgames.framework.Input.TouchEvent;

public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public List<TouchEvent> getTouchEvents();

    void setScaleX(float scaleX);
    void setScaleY(float scaleY);

    void setOffsetX(float offsetX);
    void setOffsetY(float offsetY);

    public boolean isPointerJustDown(int pointer);
    public boolean isPointerJustReleased(int pointer);
}
