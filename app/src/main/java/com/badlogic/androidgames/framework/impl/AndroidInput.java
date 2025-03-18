package com.badlogic.androidgames.framework.impl;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;

import com.badlogic.androidgames.framework.Input;

public class AndroidInput implements Input {    
    AccelerometerHandler accelHandler;
    KeyboardHandler keyHandler;
    TouchHandler touchHandler;

    private float offsetX;
    private float offsetY;
    public float width;
    public float height;
    public float targetWidth;
    public float targetHeight;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);
        keyHandler = new KeyboardHandler(view);               
        if(Integer.parseInt(VERSION.SDK) < 5) 
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);        
    }

    @Override
    public void setOffsetX(float offset) {
        offsetX = offset;
    }

    @Override
    public void setOffsetY(float offset) {
        offsetY = offset;
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer) / 6;
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer) / 6;
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
}
