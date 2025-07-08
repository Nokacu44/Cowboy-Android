package com.example.mfaella.physicsapp.managers;

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

//            loadPixmap("characters/cowboy.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("characters/hangman1.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("characters/bandit.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("guns/rifle.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("guns/bullet.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("environment/rock1.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("environment/crate.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("environment/rope_segment.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("environment/golden_chain.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("environment/platform_0.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("environment/arc_platform.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("environment/gallows.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("environment/ground.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("ui/bang_btn.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("ui/status_icons.png", Graphics.PixmapFormat.ARGB8888);
//            loadPixmap("ui/clock_sheet.png", Graphics.PixmapFormat.ARGB8888);

            Log.d("ASSETS ", String.valueOf(pixmapMap));
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