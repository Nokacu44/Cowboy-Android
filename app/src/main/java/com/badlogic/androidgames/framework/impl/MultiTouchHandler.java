/** Very similar to B.A.G.
 * Minor changes by Marco Faella, marfaella@gmail.com
 */

package com.badlogic.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Pool.PoolObjectFactory;

public class MultiTouchHandler implements TouchHandler {
    boolean[] isTouching = new boolean[20];
    boolean[] wasTouching = new boolean[20];  // stato precedente dei tocchi


    int[] touchX = new int[20];
    int[] touchY = new int[20];


    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    float scaleX;
    float scaleY;
    private float offsetX;
    private float offsetY;

    private static final int MAXPOOLSIZE = 100;

    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        touchEventPool = new Pool<TouchEvent>(factory, MAXPOOLSIZE);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public synchronized boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        TouchEvent touchEvent;
        // Aggiorna lo stato precedente
        System.arraycopy(isTouching, 0, wasTouching, 0, 20);
        switch (action) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN:
            touchEvent = touchEventPool.newObject();
            touchEvent.type = TouchEvent.TOUCH_DOWN;
            touchEvent.pointer = pointerId;
            touchEvent.x = touchX[pointerId] = (int) (((event
                    .getX(pointerIndex) - offsetX) * scaleX));
            touchEvent.y = touchY[pointerId] = (int) ((event
                    .getY(pointerIndex) - offsetY)* scaleY);
            isTouching[pointerId] = true;
            touchEventsBuffer.add(touchEvent);

            // Aggiorna lo stato precedente
            wasTouching[pointerId] = false;  // Non è mai stato toccato prima

            break;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
        case MotionEvent.ACTION_CANCEL:
            touchEvent = touchEventPool.newObject();
            touchEvent.type = TouchEvent.TOUCH_UP;
            touchEvent.pointer = pointerId;
            touchEvent.x = touchX[pointerId] = (int) (((event
                    .getX(pointerIndex) - offsetX) * scaleX));
            touchEvent.y = touchY[pointerId] = (int) ((event
                    .getY(pointerIndex) - offsetY)* scaleY);
            isTouching[pointerId] = false;
            touchEventsBuffer.add(touchEvent);

            // Aggiorna lo stato precedente
            wasTouching[pointerId] = true; // il puntatore è stato rilasciato ora
            break;

        case MotionEvent.ACTION_MOVE:
            int pointerCount = event.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                pointerIndex = i;
                pointerId = event.getPointerId(pointerIndex);

                touchEvent = touchEventPool.newObject();
                touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                touchEvent.pointer = pointerId;
                touchEvent.x = touchX[pointerId] = (int) (((event
                        .getX(pointerIndex) - offsetX) * scaleX));
                touchEvent.y = touchY[pointerId] = (int) ((event
                        .getY(pointerIndex) - offsetY)* scaleY);
                touchEventsBuffer.add(touchEvent);
            }
            break;
        }




        return true;
    }

    @Override
    public synchronized  boolean isTouchDown(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return false;
        else
            return isTouching[pointer];
    }

    @Override
    public synchronized int getTouchX(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return 0;
        else
            return touchX[pointer];
    }

    @Override
    public synchronized int getTouchY(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return 0;
        else
            return touchY[pointer];
    }

    @Override
    public synchronized List<TouchEvent> getTouchEvents() {
        // empty the old list and return the events to the pool
        for (TouchEvent event: touchEvents)
                touchEventPool.free(event);
        touchEvents.clear();

        // swap the lists
        List<TouchEvent> temp = touchEvents;
        touchEvents = touchEventsBuffer;
        touchEventsBuffer = temp;

        /* old:
        // copy the event buffer into the list
        touchEvents.addAll(touchEventsBuffer);
        touchEventsBuffer.clear();
        */

        return touchEvents;
    }

    @Override
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    @Override
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }


    @Override
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public synchronized boolean isPointerJustDown(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return false;

        // Il puntatore è appena stato toccato se è toccato ora e non lo era prima
        if (isTouching[pointer] && !wasTouching[pointer]) {
            // Dopo il primo rilevamento di un tocco, ripristina lo stato di wasTouching
            wasTouching[pointer] = true;  // Imposta il flag per non farlo ripetere nei cicli successivi
            return true;
        }

        return false;
    }

    @Override
    public synchronized boolean isPointerJustReleased(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return false;

        // Il puntatore è appena stato rilasciato se non è più toccato ora, ma lo era prima
        if (!isTouching[pointer] && wasTouching[pointer]) {
            // Dopo il rilascio, ripristina lo stato di wasTouching
            wasTouching[pointer] = false;  // Imposta il flag per non farlo ripetere nei cicli successivi
            return true;
        }

        return false;
    }

}
