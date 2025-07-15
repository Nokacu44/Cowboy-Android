package com.example.armando.game.actors.ui;

import com.example.armando.game.Coordinates;
import com.example.armando.game.actors.Actor;
import com.example.armando.game.components.SpriteComponent;
import com.example.armando.game.levels.GameLevel;
import com.example.armando.game.levels.LevelSelection;
import com.example.armando.game.managers.AudioManager;
import com.example.armando.game.managers.PixmapManager;

import java.util.ArrayList;
import java.util.List;

public class LevelWin extends Actor {

    ArrayList<SheriffStar> starsActor = new ArrayList<>();

    public LevelWin(GameLevel level, int stars) {
        super(level, 0, 0);

        level.addActor(new Actor(level, (float) Coordinates.gameWidth / 2, (float) Coordinates.gameHeight / 2, List.of(
                new SpriteComponent(PixmapManager.getPixmap("ui/dark_shadow.png"))
        )));

        starsActor.add((new SheriffStar(level, 64, 64, false)));
        starsActor.add(new SheriffStar(level, 64 + 64 + 32, 64 - 32, false));
        starsActor.add(new SheriffStar(level, 64 + 128 + 64, 64, false));

        for (Actor actor : starsActor) {
            level.addActor(actor);
        }

        for (int i = 0; i < stars; i++) {
            starsActor.get(i).makeGold();
        }

        Button retryButton = new Button(level, 160, 100, "ui/next_btn.png", () -> level.game.getLevelManager().startLevel(LevelSelection::new));
        level.addActor(retryButton);

        AudioManager.getSound("audio/win_jingle.mp3").play(1f);

    }
}
