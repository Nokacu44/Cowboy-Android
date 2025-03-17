package com.example.mfaella.physicsapp.actors;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.Managers.PixmapManager;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.List;

public class Gun extends Actor {

    Input input;
    World physicsWorld;

    float target_x = x;
    float target_y = y;

    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private int currentBullet = 0;

    private final PathEffect dotEffect = new DashPathEffect(new float[]{2f, 4f}, 0f);

    public Gun(float x, float y, Input input, World world, List<Bullet> bullets) {
        super(x, y);
        this.input = input;
        this.physicsWorld = world;
        this.currentBullet = 0;
        this.bullets.addAll(bullets);
        addComponent(new SpriteComponent(PixmapManager.getPixmap("guns/rifle.png")));
    }


    public void shoot() {
        if (currentBullet >= this.bullets.size()) return;
        Bullet bullet = this.bullets.get(currentBullet);
        bullet.getComponent(PhysicsComponent.class).body.setTransform(new Vec2(Coordinates.toSimulationX(x), Coordinates.toSimulationY(y)), (angle));
        float dirX = (float) Math.cos(angle) * 16;
        float dirY = (float) Math.sin(angle) * 16;
        bullet.shoot(dirX, dirY);
        currentBullet += 1;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (input.isTouchDown(0)) {
            this.target_x = input.getTouchX(0);
            this.target_y = input.getTouchY(0);
            rotateTowards(x, y, target_x, target_y);
            angle = clamp(angle, (float) Math.toRadians(-35), (float) Math.toRadians(35));
        }

    }


    @Override
    public void draw(Graphics g) {
        if (this.angle >= Math.toRadians(-35) && this.angle <= Math.toRadians(35)) {

            float x2 = x + 100 * (float) Math.cos(this.angle);
            float y2 = y + 100 * (float) Math.sin(this.angle);

            g.drawLine(x, y, x2, y2, Color.RED, dotEffect);

//            physicsWorld.rayCast(new RayCastCallback() {
//                @Override
//                public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
//                    Actor actor = (Actor) fixture.getBody().getUserData();
//                    Log.d("RAYCAST", (String.valueOf(actor)));
//                    return 1;
//                }
//            }, Coordinates.toSimulationX(shootPointX), Coordinates.toSimulationY(shootPointY), Coordinates.toSimulationX(x2), Coordinates.toSimulationY(y2));

        }
        super.draw(g);

    }

    public void rotateTowards(float x, float y, float targetX, float targetY) {
        float dx = targetX - (x); // Target X - Sprite Center X
        float dy = targetY - (y); // Target Y - Sprite Center Y
        angle = (float) Math.atan2(dy, dx); // Convert to degrees
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }
}
