package com.example.mfaella.physicsapp.levels;

import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.actors.ui.SheriffStar;
import com.example.mfaella.physicsapp.components.ClickableComponent;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

import java.util.List;

public class LevelSelection extends GameLevel{
    Actor sheriffStar;
    Actor brokenGlass;
    boolean beginPressed = false;

    public LevelSelection(Game game) {
        super(game);


        addActor(new Actor(this, 16 + 48 + 24, 48, List.of(new SpriteComponent(PixmapManager.getPixmap("ui/level_selection_1.png")))));
        addActor(new Actor(this, 16 + 48 * 2 + 48, 48, List.of(new SpriteComponent(PixmapManager.getPixmap("ui/level_selection_2.png")))));
        addActor(new Actor(this, 16 + 48 * 3 + 48 + 24, 48, List.of(new SpriteComponent(PixmapManager.getPixmap("ui/level_selection_3.png")))));

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

    }

    private void crackGlass() {
        brokenGlass.getComponent(SpriteComponent.class).show();
        brokenGlass.x = game.getInput().getTouchX(0);
        brokenGlass.y = game.getInput().getTouchY(0);
    }

    public void input(Input input) {
        if (beginPressed) return; // Blocca tutto
        super.input(input);
        if (!beginPressed && input.isTouchJustDown(0)) {
            crackGlass();
            Log.d("MENU", "menu");
        }
    }

}
