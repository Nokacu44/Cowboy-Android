package com.example.armando.cowboy.managers;

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


            //Log.d("ASSETS ", String.valueOf(pixmapMap));
        }
    }

    public static Pixmap loadPixmap(String fileName, Graphics.PixmapFormat format) {
        return pixmapMap.computeIfAbsent(fileName, key -> graphics.newPixmap(key, format));
    }

    public static Pixmap getPixmap(String fileName) {
        Pixmap pixmap = pixmapMap.get(fileName);
        if (pixmap == null) {
            Log.w("PixmapManager", "Pixmap not loaded yet: " + fileName + " -> loading lazily");
            pixmap = loadPixmap(fileName, Graphics.PixmapFormat.ARGB8888);
        }
        return pixmap;
    }

    public static void removePixmap(String fileName) {
        Pixmap removed = pixmapMap.remove(fileName);
        if (removed != null) {
            removed.dispose();
        }
    }

    public static void clearAll() {
        for (Pixmap p : pixmapMap.values()) {
            p.dispose();
        }
        pixmapMap.clear();
    }

}