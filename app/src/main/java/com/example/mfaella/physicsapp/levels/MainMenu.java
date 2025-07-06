package com.example.mfaella.physicsapp.levels;

import android.os.Handler;
import android.os.Looper;

import com.badlogic.androidgames.framework.Game;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.actors.ui.Button;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.managers.PhysicsManager;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

import java.util.List;

public class MainMenu extends GameLevel {


    public MainMenu(Game game) {
        super(game);
//        actors.add(new Actor(64, 64, List.of(
//            new SpriteComponent(PixmapManager.getPixmap("ui/sheriff_star.png")),
//            new PhysicsComponent(BodyType.staticBody, 64, 64)
//        )));
//        actors.add(new Button(this, 160, 100, "ui/begin_btn.png", () -> {
//
//            goToLevel(new Level1(game));
//        }));
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            goToLevel(new Level1(game));
        }, 500);
    }

    private void goToLevel(GameLevel level) {
        game.setScreen(level);
    }
}
