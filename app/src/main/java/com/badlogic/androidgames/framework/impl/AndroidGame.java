package com.badlogic.androidgames.framework.impl;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.FileIO;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.example.mfaella.physicsapp.Box;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.managers.PixmapManager;

public abstract class AndroidGame extends Activity implements Game {
    AndroidFastRenderView renderView;
    protected Graphics graphics;
    protected Audio audio;
    protected Input input;
    protected FileIO fileIO;
    protected Screen screen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 1920 : 1080;
        int frameBufferHeight = isLandscape ? 1080 : 1920;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        float scaleX = (float) frameBufferWidth
                / displaymetrics.widthPixels;
        float scaleY = (float) frameBufferHeight
                / displaymetrics.heightPixels;

        final float XMIN = 0, XMAX = 16, YMIN = 0, YMAX = 9;

        Coordinates.simulationSize = new Box(XMIN, YMIN, XMAX, YMAX);
        //Coordinates.screenSize = new Box(XMIN, YMIN, metrics.widthPixels, metrics.heightPixels);

        renderView = new AndroidFastRenderView(this, frameBuffer);
        Log.d("RENDER VIEW DIMS", String.format("%s %s", renderView.getWidth(), renderView.getHeight()));
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);

        Coordinates.gameWidth = graphics.getWidth();
        Coordinates.gameHeight = graphics.getHeight();

        PixmapManager.init(graphics);

        screen = getStartScreen();

        Log.d("GAME WIDTH COORD", String.valueOf(Coordinates.gameWidth));

        setContentView(renderView);

    }
    @Override
    public void onResume() {
        super.onResume();
        screen.resume();
        renderView.resume();
    }
    @Override
    public void onPause() {
        super.onPause();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }
    public Input getInput() {
        return input;
    }

    public FileIO getFileIO() {
        return fileIO;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Audio getAudio() {
        return audio;
    }
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    public Screen getCurrentScreen() {
        return screen;
    }
}