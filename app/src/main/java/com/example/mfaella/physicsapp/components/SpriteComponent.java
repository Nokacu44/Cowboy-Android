package com.example.mfaella.physicsapp.components;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.example.mfaella.physicsapp.actors.Actor;

public class SpriteComponent extends Component{

    Pixmap spriteSheet;
    int frames = 2;
    int currentFrame = 0;
    int frameCounter = 0;
    public final int w, h;


    public SpriteComponent(Pixmap sprite) {
        this.spriteSheet = sprite;
        this.w = sprite.getWidth();
        this.h = sprite.getHeight();
        this.frames = 1;
    }

    public SpriteComponent(Pixmap spriteSheet, int w, int h, int frames) {
        this.spriteSheet = spriteSheet;
        this.frames = frames;
        this.w = w;
        this.h = h;
    }

    @Override
    public void initialize(Actor actor) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw(Graphics g) {
        frameCounter++;
        if (frameCounter % 30 == 0) currentFrame = (currentFrame + 1) % frames;
        g.drawPixmap(spriteSheet, actor.x  , actor.y , currentFrame * w, 0, w, h, actor.angle);
    }
}
