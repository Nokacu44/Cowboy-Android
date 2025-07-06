
package com.badlogic.androidgames.framework;

import android.graphics.PathEffect;

public interface Graphics {
    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);

    public void clear(int color);

    public void drawPixel(float x, float y, int color);

    public void drawLine(float x, float y, float x2, float y2, int color, PathEffect pathEffect);

    public void drawRect(float x, float y, float width, float height, int color);
    public void drawRect(float x, float y, float width, float height, int color, float angle);

    public void drawPixmap(Pixmap pixmap, float x, float y, float srcX, float srcY,
                           float srcWidth, float srcHeight);

    public void drawPixmap(Pixmap pixmap, float x, float y, float srcX, float srcY,
                           float srcWidth, float srcHeight, float rot);

    public void drawPixmap(Pixmap pixmap, float x, float y);

    public void drawPixmap(Pixmap pixmap, float x, float y, float rot);

    public int getWidth();

    public int getHeight();

    public void zoom(float factor);

}
