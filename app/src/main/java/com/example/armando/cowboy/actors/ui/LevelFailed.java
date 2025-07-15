package com.example.armando.cowboy.actors.ui;

import com.example.armando.cowboy.Coordinates;
import com.example.armando.cowboy.actors.Actor;
import com.example.armando.cowboy.components.SpriteComponent;
import com.example.armando.cowboy.levels.GameLevel;
import com.example.armando.cowboy.managers.AudioManager;
import com.example.armando.cowboy.managers.PixmapManager;

import java.util.List;

public class LevelFailed extends Actor {
    public LevelFailed(GameLevel level) {
        super(level, 0, 0);
        level.addActor(new Actor(level, (float) Coordinates.gameWidth / 2, (float) Coordinates.gameHeight / 2, List.of(
                new SpriteComponent(PixmapManager.getPixmap("ui/dark_shadow.png"))
        )));
        Button retryButton = new Button(level, 160, 100, "ui/retry_btn.png", level.game.getLevelManager()::restartLevel);
        level.addActor(retryButton);

        AudioManager.getSound("audio/man_with_harmonica.mp3").play(0.3f);
    }
}
