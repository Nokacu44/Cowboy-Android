package com.example.mfaella.physicsapp.levels;

import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.actors.ui.Button;
import com.example.mfaella.physicsapp.actors.ui.LevelSlot;
import com.example.mfaella.physicsapp.actors.ui.SheriffStar;
import com.example.mfaella.physicsapp.components.ClickableComponent;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.managers.AudioManager;
import com.example.mfaella.physicsapp.managers.GlobalScoreManager;
import com.example.mfaella.physicsapp.managers.LevelManager;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

import java.util.List;

public class LevelSelection extends GameLevel{
    Actor sheriffStar;
    Actor brokenGlass;
    boolean beginPressed = false;

    public LevelSelection(Game game) {
        super(game);

        addActor(new LevelSlot(this, 16 + 48 + 16, 48+ 48, "ui/level_selection_1.png",() -> {
            crackGlass();
            timerManager.scheduleOnce(() -> game.getLevelManager().startLevel(Level1::new), 1000);
            AudioManager.getMusic("audio/cesare_rides_again.mp3").stop();
        }, GlobalScoreManager.getStars("Level1")));

        addActor(new LevelSlot(this, 16 + 48 * 2 + 48, 48 ,"ui/level_selection_2.png",() -> {
            crackGlass();
            timerManager.scheduleOnce(() -> game.getLevelManager().startLevel(Level2::new), 1000);
            AudioManager.getMusic("audio/cesare_rides_again.mp3").stop();
        }, GlobalScoreManager.getStars("Level2")));

        addActor(new LevelSlot(this, 16 + 48 * 3 + 48 + 32, 48 + 48, "ui/level_selection_3.png",() -> {
            crackGlass();
            timerManager.scheduleOnce(() -> game.getLevelManager().startLevel(Level3::new), 1000);
            AudioManager.getMusic("audio/cesare_rides_again.mp3").stop();
        }, GlobalScoreManager.getStars("Level3")));

        sheriffStar = new SheriffStar(this, 70, 72, true);
        PhysicsComponent comp = new PhysicsComponent(this, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(64), Coordinates.pixelsToMetersLengthsY(64));

        sheriffStar.addComponent(comp);


        sheriffStar.getComponent(PhysicsComponent.class).body.setActive(false);
        sheriffStar.getComponent(SpriteComponent.class).setAnimating(false);
        sheriffStar.getComponent(SpriteComponent.class).setCurrentFrame(0);
        sheriffStar.addComponent(new ClickableComponent(game.getInput(), new RectF(sheriffStar.x - 32, sheriffStar.y - 32, sheriffStar.x + 32, sheriffStar.y + 32), () -> {
            sheriffStar.getComponent(PhysicsComponent.class).body.setActive(true);
            crackGlass();
        }));

        brokenGlass = new Actor(this, 0, 0, List.of(new SpriteComponent(PixmapManager.getPixmap("ui/broken_glass.png"))));
        actors.add(brokenGlass);
        brokenGlass.getComponent(SpriteComponent.class).hide();

        //Log.d("MENU", String.valueOf(GlobalScoreManager.getAllStars()));

        addActor(new Button(this, 32, 16, "ui/go_back_btn.png", () -> {
            game.getLevelManager().startLevel(MainMenu::new);
        }));
        if (!AudioManager.getMusic("audio/cesare_rides_again.mp3").isPlaying()) {
            AudioManager.getMusic("audio/cesare_rides_again.mp3").play();
        }

    }

    private void crackGlass() {
        brokenGlass.getComponent(SpriteComponent.class).show();
        brokenGlass.x = game.getInput().getTouchX(0);
        brokenGlass.y = game.getInput().getTouchY(0);
        AudioManager.getSound("audio/sparoFucile.wav").play(100);

    }

    public void input(Input input) {
        if (beginPressed) return; // Blocca tutto
        super.input(input);
        if (!beginPressed && input.isTouchJustDown(0)) {
            crackGlass();
        }
    }

    @Override
    public String getLevelId() {
        return "LevelSelection";
    }

}
