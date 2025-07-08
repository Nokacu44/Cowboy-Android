package com.example.mfaella.physicsapp.actors.ui;

import static com.example.mfaella.physicsapp.events.GameEvents.EventType.BANG_BUTTON_PRESSED;
import static com.example.mfaella.physicsapp.events.GameEvents.EventType.SHOOT;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.actors.Rope;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.events.GameEvents;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PixmapManager;

import java.util.List;


public class MainUI extends Actor {

    Button bang_btn;
    Rope rope;

    public MainUI(GameLevel level) {
        super(level, 0, 0);
        bang_btn = (new Button(level, 32, 158, "ui/bang_btn_sheet.png", 26, 29, 4, () -> {
            if (!level.finished) {
                level.events.emit(BANG_BUTTON_PRESSED);
            }
        }));
        rope = new Rope(level,305, 0, 6, Rope.RopeType.SOFT, Clock::new);
        level.events.connect(SHOOT, (data) -> {
            bang_btn.getComponent(SpriteComponent.class).setCurrentFrame((bang_btn.getComponent(SpriteComponent.class).getCurrentFrame() + 1) % 4);
        });
    }

    @Override
    public void update(float dt) {

        bang_btn.update(dt);
        rope.update(dt);
    }

    @Override
    public void draw(Graphics g) {
        bang_btn.draw(g);
        rope.draw(g);
    }
}
