package com.example.armando.cowboy.actors.ui;

import static com.example.armando.cowboy.events.GameEvents.EventType.BANG_BUTTON_PRESSED;
import static com.example.armando.cowboy.events.GameEvents.EventType.SHOOT;

import com.badlogic.androidgames.framework.Graphics;
import com.example.armando.cowboy.actors.Actor;
import com.example.armando.cowboy.actors.Rope;
import com.example.armando.cowboy.components.SpriteComponent;
import com.example.armando.cowboy.levels.GameLevel;
import com.example.armando.cowboy.levels.LevelSelection;


public class MainUI extends Actor {

    Button bang_btn;
    Button retry_btn;
    Rope rope;

    public MainUI(GameLevel level) {
        super(level, 0, 0);
        bang_btn = (new Button(level, 32, 158, "ui/bang_btn_sheet.png", 26, 29, 4, () -> {
            if (!level.finished) {
                level.events.emit(BANG_BUTTON_PRESSED);
            }
        }));
        rope = new Rope(level, 305, 0, 6, Rope.RopeType.HARD, Clock::new);
        level.events.connect(SHOOT, (data) -> {
            bang_btn.getComponent(SpriteComponent.class).setCurrentFrame((bang_btn.getComponent(SpriteComponent.class).getCurrentFrame() + 1) % 4);
        });

        retry_btn = (new Button(level, 24, 38, "ui/retry_small_btn.png", 36, 18, 1, () -> {
            if (!level.finished) {
                level.game.getLevelManager().restartLevel();
            }
        }));

        level.addActor(new Button(level, 24, 16, "ui/go_back_btn.png", () -> {
            level.game.getLevelManager().startLevel(LevelSelection::new);
        }));
    }

    @Override
    public void update(float dt) {

        bang_btn.update(dt);
        rope.update(dt);
        retry_btn.update(dt);
    }

    @Override
    public void draw(Graphics g) {
        bang_btn.draw(g);
        rope.draw(g);
        retry_btn.draw(g);
    }
}
