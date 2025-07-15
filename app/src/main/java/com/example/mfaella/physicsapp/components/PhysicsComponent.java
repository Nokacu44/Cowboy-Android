package com.example.mfaella.physicsapp.components;

import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.CollisionHandler;
import com.example.mfaella.physicsapp.Coordinates;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PhysicsManager;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.DistanceJoint;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.JointDef;
import com.google.fpl.liquidfun.JointType;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.RevoluteJointDef;
import com.google.fpl.liquidfun.RopeJointDef;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;

public class PhysicsComponent extends Component {

    public static class JointInfo {
        public final Joint joint;
        public final PhysicsComponent other;

        public JointInfo(Joint joint, PhysicsComponent other) {
            this.joint = joint;
            this.other = other;
        }
    }
    public final Body body;

    public final float width;
    public final float height;

    private CollisionHandler handler;

    private final ArrayList<JointInfo> joints = new ArrayList<>();

    public PhysicsComponent(GameLevel level, BodyType bodyType, float width, float height, float density, CollisionHandler collisionHandler) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(bodyType);

        body = level.physicsManager.physicsWorld.createBody(bodyDef);
        body.setSleepingAllowed(false);
        bodyDef.setPosition(Coordinates.toSimulationX(-1), Coordinates.toSimulationY(-1));
        PolygonShape box = new PolygonShape();

        this.width = width;
        this.height = height;
        box.setAsBox(this.width / 2, this.height / 2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(0.2f);       // default 0.2
        fixturedef.setRestitution(0.4f);    // default 0
        fixturedef.setDensity(density);     // default 0

        body.createFixture(fixturedef);

        handler = collisionHandler;

        fixturedef.delete();
        box.delete();
        bodyDef.delete();


    }

    public PhysicsComponent(
            GameLevel level, BodyType bodyType, float width, float height, float density
    ) {
        this(level, bodyType, width, height, density, null);
    }

    public PhysicsComponent(
            GameLevel level, BodyType bodyType, float width, float height
    ) {
        this(level, bodyType, width, height, 0.5f, null);
    }
    public PhysicsComponent(
            GameLevel level, BodyType bodyType, float width, float height, CollisionHandler handler
    ) {
        this(level, bodyType, width, height, 0.5f, handler);
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


    public void onCollision(Body myBody, Body otherBody) {
        Actor otherActor = (Actor) otherBody.getUserData();
        if (handler != null) {
            handler.handleCollision(otherActor, myBody, otherBody);
        }
    }

    public void setCollisionHandler(CollisionHandler handler) {
        this.handler = handler;
    }

    public void addRopeJoint(
            PhysicsComponent other,
            Vec2 anchorThis,
            Vec2 anchorOther,
            float maxLength
    ) {
        RopeJointDef jointDef = new RopeJointDef();

        // Gli anchor sono relativi ai rispettivi corpi
        jointDef.setLocalAnchorA(anchorThis.getX(), anchorThis.getY());
        jointDef.setLocalAnchorB(anchorOther.getX(), anchorOther.getY());

        jointDef.setBodyA(this.body);
        jointDef.setBodyB(other.body);

        jointDef.setMaxLength(maxLength);


        // Crea il joint
        Joint joint = level.physicsManager.physicsWorld.createJoint(jointDef);
        registerJoint(joint, other);


        jointDef.delete();
    }

    public void addDistanceJoint(
            PhysicsComponent other,
            Vec2 anchorThis,
            Vec2 anchorOther,
            float length
    ) {
        DistanceJointDef jointDef = new DistanceJointDef();

        // Gli anchor sono relativi ai rispettivi corpi
        jointDef.setLocalAnchorA(anchorThis.getX(), anchorThis.getY());
        jointDef.setLocalAnchorB(anchorOther.getX(), anchorOther.getY());

        jointDef.setBodyA(this.body);
        jointDef.setBodyB(other.body);

        jointDef.setLength(length);
        jointDef.setDampingRatio(0.5f);
        jointDef.setFrequencyHz(4.0f);

        // Crea il joint
        Joint joint = level.physicsManager.physicsWorld.createJoint(jointDef);
        registerJoint(joint, other);


        jointDef.delete();
    }

    public void addJoint(
            PhysicsComponent other,
            Vec2 anchorThis,
            Vec2 anchorOther,
            boolean enableLimits,
            float lowerAngle,
            float upperAngle
    ) {
        RevoluteJointDef jointDef = new RevoluteJointDef();

        // Gli anchor sono relativi ai rispettivi corpi
        jointDef.setLocalAnchorA(anchorThis.getX(), anchorThis.getY());
        jointDef.setLocalAnchorB(anchorOther.getX(), anchorOther.getY());

        jointDef.setBodyA(this.body);
        jointDef.setBodyB(other.body);

        // Abilita limiti se richiesto
        if (enableLimits) {
            jointDef.setEnableLimit(true);
            jointDef.setLowerAngle(lowerAngle);
            jointDef.setUpperAngle(upperAngle);
            //jointDef.setMaxMotorTorque(0.2f);
        }

        // Crea il joint
        Joint joint = level.physicsManager.physicsWorld.createJoint(jointDef);
        registerJoint(joint, other);

        jointDef.delete();
    }

    public void clearJoints() {
        for (JointInfo info : joints) {
            info.other.joints.removeIf(j -> j.joint == info.joint);
            //Log.d("ROPE", "sto eliminando");
            level.physicsManager.physicsWorld.destroyJoint(info.joint);
        }
        joints.clear();
        //Log.d("ROPE", String.valueOf(joints.size()));

    }

    private void registerJoint(Joint joint, PhysicsComponent other) {
        this.joints.add(new JointInfo(joint, other));
        other.joints.add(new JointInfo(joint, this));
    }

}
