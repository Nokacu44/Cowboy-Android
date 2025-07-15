package com.example.armando.game.components;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.example.armando.game.actors.Actor;

public class SpriteComponent extends Component {

    private final Pixmap spriteSheet;
    public final int frameWidth, frameHeight;
    private final int frameCount;

    private int currentFrame = 0;
    private float frameTime = 0f;
    private float frameDuration = 0.1f; // Durata di un frame in secondi (default: 10 fps)

    private boolean visible = true;
    private boolean looping = true;
    private boolean animating = true;

    public SpriteComponent(Pixmap sprite) {
        this(sprite, sprite.getWidth(), sprite.getHeight(), 1);
    }

    public SpriteComponent(Pixmap spriteSheet, int frameWidth, int frameHeight, int frameCount) {
        this.spriteSheet = spriteSheet;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameCount = frameCount;
    }

    public void setFps(float fps) {
        if (fps <= 0) return;
        this.frameDuration = 1.0f / fps;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

    @Override
    public void initialize(Actor actor) {
        this.currentFrame = 0;
        this.frameTime = 0f;
    }

    @Override
    public void update(float dt) {
        if (!animating) return;
        if (frameCount > 1) {
            frameTime += dt;
            if (frameTime >= frameDuration) {
                frameTime -= frameDuration;
                currentFrame++;
                if (currentFrame >= frameCount) {
                    if (looping) {
                        currentFrame = 0;
                    } else {
                        currentFrame = frameCount - 1;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (visible) {
            g.drawPixmap(
                    spriteSheet,
                    actor.x,
                    actor.y,
                    currentFrame * frameWidth,
                    0,
                    frameWidth,
                    frameHeight,
                    actor.angle
            );
        }
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int frame) {
        if (frame < 0 || frame >= frameCount)
            return;
        this.currentFrame = frame;
        this.frameTime = 0f;
    }

}
