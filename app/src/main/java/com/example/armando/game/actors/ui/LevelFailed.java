package com.example.armando.game.actors.ui;

import com.example.armando.game.Coordinates;
import com.example.armando.game.actors.Actor;
import com.example.armando.game.components.SpriteComponent;
import com.example.armando.game.levels.GameLevel;
import com.example.armando.game.managers.AudioManager;
import com.example.armando.game.managers.PixmapManager;

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
