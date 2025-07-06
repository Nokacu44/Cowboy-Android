package com.badlogic.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.animation.ValueAnimator;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.google.fpl.liquidfun.Vec2;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    public Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    RectF dstRect = new RectF();
    private final int SCALE_FACTOR = 6;

    private float currentTranslateX;
    private float currentTranslateY;
    private float currentScale;

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.canvas.scale(SCALE_FACTOR, SCALE_FACTOR);
        this.paint = new Paint();
        this.paint.setAntiAlias(false);
    }
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config = null;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);
    }
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawPixel(float x, float y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }
    @Override
    public void drawLine(float x, float y, float x2, float y2, int color, PathEffect pathEffect) {
        paint.setColor(color);
        paint.setStrokeWidth(1);
        paint.setPathEffect(pathEffect);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);

        // Calcolare la posizione per centrare il rettangolo
        float left = x - width / 2;
        float top = y - height / 2;
        float right = x + width / 2;
        float bottom = y + height / 2;

        // Disegnare il rettangolo
        canvas.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color, float angle) {
        canvas.save();
        canvas.rotate(angle, width / 2, height / 2);
        drawRect(x, y, width, height, color);
        canvas.restore();
    }

    @Override
    public void drawPixmap(Pixmap pixmap, float x, float y, float srcX, float srcY,
                           float srcWidth, float srcHeight) {
        srcRect.left = (int)srcX;
        srcRect.top = (int)srcY;
        srcRect.right = (int) (srcX + srcWidth);
        srcRect.bottom = (int) (srcY + srcHeight);

        dstRect.left = (x - srcWidth / 2);
        dstRect.top = (y - srcHeight / 2);
        dstRect.right = (x + srcWidth / 2);
        dstRect.bottom = (y + srcHeight / 2);


        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, float x, float y, float srcX, float srcY, float srcWidth, float srcHeight, float rot) {
        this.canvas.save();
        this.canvas.rotate((float) Math.toDegrees(rot), x , y);
        this.drawPixmap(pixmap, x, y, srcX, srcY, srcWidth, srcHeight);
        this.canvas.restore();
    }

    @Override
    public void drawPixmap(Pixmap pixmap, float x, float y) {
        float width = pixmap.getWidth();
        float height = pixmap.getHeight();

        // Calcolare la posizione per centrare l'immagine
        float left = x - width / 2;
        float top = y - height / 2;

        // Disegnare l'immagine
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, left, top, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, float x, float y, float rot) {
        this.canvas.save();
        float semiWidth = (float) pixmap.getWidth() / 2;
        float semiHeight = (float) pixmap.getHeight() / 2;
        this.canvas.rotate((float) Math.toDegrees(rot), x , y);
        dstRect.left = x - semiWidth;
        dstRect.bottom = y + semiHeight;
        dstRect.right = x + semiWidth;
        dstRect.top = y - semiHeight;
        this.canvas.restore();
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth() / SCALE_FACTOR;
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight() / SCALE_FACTOR;
    }

    public void zoom(float factor) {
        this.canvas.scale(factor, factor);
    }

}