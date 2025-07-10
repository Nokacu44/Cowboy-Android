package com.example.mfaella.physicsapp.actors.ui;

import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.AudioManager;
import com.example.mfaella.physicsapp.managers.PixmapManager;

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
