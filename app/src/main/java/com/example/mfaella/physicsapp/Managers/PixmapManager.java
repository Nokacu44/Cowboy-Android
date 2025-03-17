package com.example.mfaella.physicsapp.Managers;

import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.HashMap;
import java.util.Map;

public class PixmapManager {

    private static final Map<String, Pixmap> pixmapMap = new HashMap<>();
    private static Graphics graphics;

    public static void init(Graphics graphics) {
        if (PixmapManager.graphics == null) {
            PixmapManager.graphics = graphics;

            loadPixmap("characters/cowboy.png", Graphics.PixmapFormat.ARGB8888);
            loadPixmap("characters/hangman1.png", Graphics.PixmapFormat.ARGB8888);
            loadPixmap("guns/rifle.png", Graphics.PixmapFormat.ARGB8888);
            loadPixmap("guns/bullet.png", Graphics.PixmapFormat.ARGB8888);
            loadPixmap("environment/rock1.png", Graphics.PixmapFormat.ARGB8888);
            loadPixmap("environment/crate.png", Graphics.PixmapFormat.ARGB8888);

            Log.d("ASSETS ", String.valueOf(pixmapMap));
        }
    }

    public static Pixmap loadPixmap(String fileName, Graphics.PixmapFormat format) {
        if (pixmapMap.containsKey(fileName)) {
            return pixmapMap.get(fileName);
        }

        Pixmap newPixmap = graphics.newPixmap(fileName, format);
        pixmapMap.put(fileName, newPixmap);
        return newPixmap;
    }

    public static Pixmap getPixmap(String fileName) {
        return pixmapMap.get(fileName);
    }

    public static void removePixmap(String fileName) {
        pixmapMap.remove(fileName);
    }

    public static void clearAll() {
        pixmapMap.clear();
    }
}