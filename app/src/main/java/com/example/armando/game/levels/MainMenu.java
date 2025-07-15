package com.example.armando.game.levels;

import android.graphics.RectF;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Music;
import com.example.armando.game.Coordinates;
import com.example.armando.game.actors.Actor;
import com.example.armando.game.actors.ui.Button;
import com.example.armando.game.actors.ui.SheriffStar;
import com.example.armando.game.components.ClickableComponent;
import com.example.armando.game.components.PhysicsComponent;
import com.example.armando.game.components.SpriteComponent;
import com.example.armando.game.managers.AudioManager;
import com.example.armando.game.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

import java.util.List;

public class MainMenu extends GameLevel {
    Actor brokenGlass;
    Actor sheriffStar;
    boolean beginPressed = false;
    boolean starBroken = false;

    Music backgroundMusic;

    public MainMenu(Game game) {
        super(game);
        addActor(new Actor(this, (float) Coordinates.gameWidth / 2, (float) Coordinates.gameHeight / 2, List.of(
                new SpriteComponent(PixmapManager.getPixmap("ui/title.png"))
        )));
        sheriffStar = new SheriffStar(this, 70, 72, true);
        PhysicsComponent comp = new PhysicsComponent(this, BodyType.dynamicBody, Coordinates.pixelsToMetersLengthsX(64), Coordinates.pixelsToMetersLengthsY(64));

        sheriffStar.addComponent(comp);


        sheriffStar.getComponent(PhysicsComponent.class).body.setActive(false);
        sheriffStar.getComponent(SpriteComponent.class).setAnimating(false);
        sheriffStar.getComponent(SpriteComponent.class).setCurrentFrame(0);
        sheriffStar.addComponent(new ClickableComponent(game.getInput(), new RectF(sheriffStar.x - 32, sheriffStar.y - 32, sheriffStar.x + 32, sheriffStar.y + 32), () -> {
            sheriffStar.getComponent(PhysicsComponent.class).body.setActive(true);
            if (!starBroken) {
                timerManager.scheduleOnce(() -> {
                    sheriffStar.getComponent(PhysicsComponent.class).body.setAwake(false);
                    sheriffStar.getComponent(PhysicsComponent.class).body.setActive(false);
                }, 3000);
                starBroken = true;
            }

            crackGlass();
        }));

        actors.add(sheriffStar);

        actors.add(new Button(this, 160, 120, "ui/begin_btn.png", () -> {
            if (beginPressed) return;
            crackGlass();
            beginPressed = true;
            active = false; // blocca tutto
            timerManager.scheduleOnce(() -> {
                game.getLevelManager().startLevel(LevelSelection::new);
            }, 1000);
        }));

        brokenGlass = new Actor(this, 0, 0, List.of(new SpriteComponent(PixmapManager.getPixmap("ui/broken_glass.png"))));
        actors.add(brokenGlass);
        brokenGlass.getComponent(SpriteComponent.class).hide();

        backgroundMusic = AudioManager.getMusic("audio/cesare_rides_again.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    private void crackGlass() {
        brokenGlass.getComponent(SpriteComponent.class).show();
        brokenGlass.x = game.getInput().getTouchX(0);
        brokenGlass.y = game.getInput().getTouchY(0);
        AudioManager.getSound("audio/sparoFucile.wav").play(100);
    }

    @Override
    public void input(Input input) {
        if (beginPressed) return; // Blocca tutto
        super.input(input);
        if (!beginPressed && input.isTouchJustDown(0)) {
            crackGlass();
            //Log.d("MENU", "menu");
        }
    }

    @Override
    public String getLevelId() {
        return "MainMenu";
    }

    @Override
    public void pause() {
        super.pause();
        backgroundMusic.pause();
    }

    @Override
    public void resume() {
        super.resume();
        backgroundMusic.play();
    }
}
