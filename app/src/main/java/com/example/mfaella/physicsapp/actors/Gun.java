package com.example.mfaella.physicsapp.actors;

import static com.example.mfaella.physicsapp.events.GameEvents.EventType.BANG_BUTTON_PRESSED;
import static com.example.mfaella.physicsapp.events.GameEvents.EventType.BEGIN_AIM;
import static com.example.mfaella.physicsapp.events.GameEvents.EventType.HANGMAN_DEAD;
import static com.example.mfaella.physicsapp.events.GameEvents.EventType.OUT_OF_AMMO;
import static com.example.mfaella.physicsapp.events.GameEvents.EventType.SHOOT;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.events.GameEvents;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.AudioManager;
import com.example.mfaella.physicsapp.managers.PhysicsManager;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Gun extends Actor {

    private final float MIN_ANGLE = -40;
    private final float MAX_ANGLE = 40;

    Input input;
    World physicsWorld;

    float target_x = x;
    float target_y = y;

    public boolean canShoot = true;

    private final ArrayList<Bullet> bullets;
    private int currentBullet;

    private final PathEffect dotEffect = new DashPathEffect(new float[]{2f, 4f}, 0f);

    public Gun(GameLevel level, float x, float y, Input input, int rounds) {
        super(level, x, y);
        this.input = input;

        bullets = new ArrayList<>(rounds);
        for (int i = 0; i < rounds; ++i) {
            bullets.add(new Bullet(level));
        }

        this.physicsWorld = level.physicsManager.physicsWorld;
        this.currentBullet = 0;
        addComponent(new SpriteComponent(PixmapManager.getPixmap("guns/rifle.png")));

        level.events.connect(BEGIN_AIM, (data) -> {
            AudioManager.getSound("audio/chambering.mp3").play(100);
        });

    }


    public void shoot() {
        if (currentBullet >= this.bullets.size()) {
            canShoot = false;
            return;
        }
        Bullet bullet = this.bullets.get(currentBullet);
        bullet.getComponent(PhysicsComponent.class).body.setTransform(new Vec2(Coordinates.toSimulationX(x), Coordinates.toSimulationY(y)), (angle));
        //this.rotateTowards(x, y, target_x, target_y);
        float dirX = (float) Math.cos(angle) * 16;
        float dirY = (float) Math.sin(angle) * 16;
        Log.d("DIRECTION ", String.format("%s %s", dirX, dirY));
        bullet.activate();
        bullet.shoot(dirX, dirY);
        currentBullet += 1;
        level.events.emit(SHOOT);
        if (currentBullet >= this.bullets.size()) {
            level.events.emit(OUT_OF_AMMO);
        }
        AudioManager.getSound("audio/sparoFucile2.mp3").play(100);

    }


    @Override
    public void update(float dt) {
        super.update(dt);
        if (level.finished) return;
        if (canShoot && input.isTouchDown(0) && input.getTouchX(0) > x) {
            if (input.isTouchJustDown(0)) {
                level.events.emit(BEGIN_AIM);
            }
            this.target_x = input.getTouchX(0);
            this.target_y = input.getTouchY(0);
            rotateTowards(x, y, target_x, target_y);
            angle = clamp(angle, (float) Math.toRadians(MIN_ANGLE), (float) Math.toRadians(MAX_ANGLE));
        }
        for (int i = 0; i < this.bullets.size(); i++) {
            this.bullets.get(i).update(dt);
        }
    }


    @Override
    public void draw(Graphics g) {
        if (this.angle >= Math.toRadians(MIN_ANGLE) && this.angle <= Math.toRadians(MAX_ANGLE)) {

            float dirX = (float) Math.cos(angle) * 16;
            float dirY = (float) Math.sin(angle) * 16;

            List<Vec2> points = calculateTrajectory(
                    new Vec2(Coordinates.toSimulationX(x), Coordinates.toSimulationY(y)),
                    new Vec2((dirX * 16 / 250 ) , (dirY * 16 / 250)),
                    0.04f,
                    level.physicsManager.gravity,
                    0.25f,          // quanti punti
                    20        // ogni quanti secondi
            );
//
            for (Vec2 p : points) {
                g.drawRect(Coordinates.toPixelsX(p.getX()), Coordinates.toPixelsX(p.getY()), 1, 1, Color.RED);
            }
        }
        super.draw(g);
        for (int i = 0; i < this.bullets.size(); i++) {
            this.bullets.get(i).draw(g);
        }
    }

    public void rotateTowards(float x, float y, float targetX, float targetY) {
        float dx = targetX - (x); // Target X - Sprite Center X
        float dy = targetY - (y); // Target Y - Sprite Center Y
        angle = (float) Math.atan2(dy, dx); // Convert to degrees
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }


    private List<Vec2> calculateTrajectory(
            Vec2 startPosition,
            Vec2 impulse,
            float bodyMass,
            Vec2 gravity,
            float totalDuration,  // es: 2 secondi
            int pointCount)       // es: 20 punti
    {
        List<Vec2> trajectoryPoints = new ArrayList<>();

        // Convert impulse to initial velocity
        Vec2 initialVelocity = new Vec2(impulse.getX() / bodyMass, impulse.getY() / bodyMass);


        float timeStep = totalDuration / pointCount;

        for (int i = 0; i < pointCount; i++) {
            float t = i * timeStep;

            // position = start + v * t + 0.5 * g * t^2
            float x = startPosition.getX() + initialVelocity.getX() * t + 0.5f * gravity.getX() * t * t;
            float y = startPosition.getY() + initialVelocity.getY() * t + 0.5f * gravity.getY() * t * t;

            trajectoryPoints.add(new Vec2(x, y));
        }
        return trajectoryPoints;
    }
}
