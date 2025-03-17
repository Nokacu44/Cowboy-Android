package com.example.mfaella.physicsapp.components;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.actors.Actor;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.World;

public class PhysicsComponent extends Component {

    public final Body body;

    public final float width;
    public final float height;

    public PhysicsComponent(World physicsWorld, BodyType bodyType, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(bodyType);

        body = physicsWorld.createBody(bodyDef);
        body.setSleepingAllowed(false);
        bodyDef.setPosition(Coordinates.toSimulationX(-1), Coordinates.toSimulationY(-1));
        PolygonShape box = new PolygonShape();

        this.width = width;
        this.height = height;
        box.setAsBox(this.width / 2, this.height / 2);
        //box.setAsBox(this.width / 2, this.height / 2, width / 2, -height / 2, 0);

        FixtureDef fixturedef = new FixtureDef();

        fixturedef.setShape(box);
        fixturedef.setFriction(0.2f);       // default 0.2
        fixturedef.setRestitution(0.4f);    // default 0
        fixturedef.setDensity(0.5f);     // default 0
        body.createFixture(fixturedef);

        fixturedef.delete();
        box.delete();
        bodyDef.delete();

    }

    @Override
    public void initialize(Actor actor) {
        body.setTransform(
                Coordinates.toSimulationX(actor.x),
                Coordinates.toSimulationY(actor.y)
        , actor.angle);
        body.setUserData(actor);
    }

    @Override
    public void update(float dt) {
        // E' giusto in quanto esse sono utilizzate solo in fase di rendering
        actor.x = Coordinates.toPixelsX(body.getPositionX());
        actor.y = Coordinates.toPixelsY(body.getPositionY());
        actor.angle = body.getAngle();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }
}
