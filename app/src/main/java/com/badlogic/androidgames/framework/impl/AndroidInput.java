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

    public float scaleX;
    public float scaleY;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);
        keyHandler = new KeyboardHandler(view);               
        if(Integer.parseInt(VERSION.SDK) < 5) 
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);        
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
    public boolean isTouchJustDown(int pointer) {
        return touchHandler.isPointerJustDown(pointer);
    }

    @Override
    public boolean isTouchJustRelease(int pointer) {
        return touchHandler.isPointerJustReleased(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
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
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        this.touchHandler.setScaleX(scaleX);
    }

    @Override
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        this.touchHandler.setScaleY(scaleY);
    }

    @Override
    public void setOffsetX(float offsetX) {
        this.touchHandler.setOffsetX(offsetX);
    }

    @Override
    public void setOffsetY(float offsetY) {
        this.touchHandler.setOffsetY(offsetY);
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
