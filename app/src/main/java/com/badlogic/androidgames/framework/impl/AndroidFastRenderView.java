package com.badlogic.androidgames.framework.impl;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.armando.game.Coordinates;

@SuppressLint("ViewConstructor")
public class AndroidFastRenderView extends SurfaceView implements Runnable {

    AndroidGame game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    Rect dstRect;
    Rect srcRect = new Rect();
    volatile boolean running = false;


    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.dstRect = new Rect();
        this.framebuffer = framebuffer;
        this.holder = getHolder();
        this.holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int width, int height) {
                Log.d("WIDTH: ", String.valueOf(width));
                final float TARGET_RATIO = 16.0f / 9.0f;
                final int BASE_WIDTH = 320;  // Larghezza base (multiplo)
                final int BASE_HEIGHT = 180; // Altezza base (multiplo)

                // Calcola la larghezza e l'altezza in base al rapporto 16:9
                int targetWidth, targetHeight;
                if ((float) width / height > TARGET_RATIO) {
                    // La schermata è più larga rispetto al 16:9 -> adatta l'altezza e centra in orizzontale
                    targetHeight = height;
                    targetWidth = (int) (height * TARGET_RATIO);
                } else {
                    // La schermata è più alta rispetto al 16:9 -> adatta la larghezza e centra in verticale
                    targetWidth = width;
                    targetHeight = (int) (width / TARGET_RATIO);
                }

                // Forza targetWidth a essere un multiplo di BASE_WIDTH (320)
                targetWidth = (targetWidth / BASE_WIDTH) * BASE_WIDTH;

                // Forza targetHeight a essere un multiplo di BASE_HEIGHT (180)
                targetHeight = (targetHeight / BASE_HEIGHT) * BASE_HEIGHT;

                int offsetX = (width - targetWidth) / 2;
                int offsetY = (height - targetHeight) / 2;

                //Log.d("GRAPHICS", String.valueOf(targetWidth));
                //Log.d("GRAPHICS", String.valueOf(targetHeight));
                //Log.d("GRAPHICS", String.valueOf(offsetX));
                //Log.d("GRAPHICS", String.valueOf(offsetY));

                // Settare finalmente scaleX e scaleY
                game.input.setScaleX((float) BASE_WIDTH / targetWidth);
                game.input.setScaleY((float) BASE_HEIGHT / targetHeight);
                game.input.setOffsetX((float) offsetX);
                game.input.setOffsetY((float) offsetY);
                Coordinates.scaleX = (float) BASE_WIDTH / targetWidth;
                Coordinates.scaleY = (float) BASE_HEIGHT / targetHeight;

                dstRect.set(offsetX, offsetY, offsetX + targetWidth, offsetY + targetHeight);
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
    }
    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }
    public void run() {
        long startTime = System.nanoTime(), fpsTime = startTime, frameCounter = 0;;
        while(running) {
            if(!holder.getSurface().isValid())
                continue;

            long currentTime = System.nanoTime();
            float deltaTime = (currentTime-startTime) / 1000000000f,
                    fpsDeltaTime = (currentTime-fpsTime) / 1000000000f;
            startTime = currentTime;

            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().present(deltaTime);

            Canvas canvas = holder.lockCanvas();
            canvas.drawBitmap(framebuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);

            frameCounter++;
            if (fpsDeltaTime > 1) { // Print every second
                //Log.d("FastRenderView", "Current FPS = " + frameCounter);
                frameCounter = 0;
                fpsTime = currentTime;
            }

        }
    }
    public void pause() {
        running = false;
        while(true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }
}