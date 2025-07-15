package com.example.armando.cowboy;

import com.example.armando.cowboy.actors.Actor;
import com.example.armando.cowboy.components.PhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by mfaella on 01/03/16.
 */
public class MyContactListener extends ContactListener {

    private Collection<Collision> cache = new HashSet<>();

    public Collection<Collision> getCollisions() {
        Collection<Collision> result = new HashSet<>(cache);
        cache.clear();
        return result;
    }


    /** Warning: this method runs inside world.step
     *  Hence, it cannot change the physical world.
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA(), fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();
        handleCollisions(ba, bb);
        handleCollisions(bb, ba);
        //Log.d("CONTACT", String.format("Collisione fra %s e %s", userdataA, userdataB));
    }

    public void handleCollisions(Body bodyA, Body bodyB) {
        Actor actorA = (Actor) bodyA.getUserData();
        Actor actorB = (Actor) bodyB.getUserData();

        PhysicsComponent physA = actorA.getComponent(PhysicsComponent.class);
        PhysicsComponent physB = actorB.getComponent(PhysicsComponent.class);

        if (physA != null) {
            physA.onCollision(bodyA, bodyB);
        }
        if (physB != null) {
            physB.onCollision(bodyB, bodyA);
        }


//        if (actorA instanceof Bullet && actorB.hasTag(Tag.ROPE_SEGMENT)) {
//            Log.d("ROPE", "segment!");
//            //Log.d("MEMORIA", String.valueOf(comp.hashCode()));
//            PhysicsComponent comp = actorB.getComponent(PhysicsComponent.class);
//            GameEvents.emit(GameEvents.EventType.ROPE_CUT);
//
//            // Necessario per cambiare il mondo fisico !
//            PhysicsManager.postTask(() -> {
//                comp.clearJoints();
//                //bodyB.setAwake(false);
//                //bodyB.setActive(false);
//            });
//            Log.d("MEMORIA", "sss");
//        }
////        if (actorA instanceof Bullet && actorB.hasComponent(PhysicsComponent.class)) {
////            PhysicsComponent comp = actorB.getComponent(PhysicsComponent.class);
////            Log.d("MEMORIA", String.valueOf(comp.hashCode()));
////        }
//
//        if (actorA instanceof Bullet) {
//            if (actorB instanceof Deflection) {
//                Log.d("COLL", "Bullet colliding");
//                // Ferma la velocità del proiettile prima di applicare l'impulso (opzionale)
//                bodyA.setLinearVelocity(new Vec2(0, 0));
//
//                // Angolo di deviazione (in radianti), ad esempio 45 gradi (pi / 4)
//                float angle = (float) Math.toRadians(((Deflection) actorB).getDeflectionAngle());  // 45 gradi -> 45 * (pi / 180)
//
//                // Calcola la direzione dell'impulso (usando il coseno e il seno per x e y)
//                float impulseX = (float) Math.cos(angle);  // Componente x dell'impulso
//                float impulseY = (float) Math.sin(angle);  // Componente y dell'impulso
//
//                // Impulso da applicare al corpo
//                Vec2 impulse = new Vec2(impulseX / 10, impulseY / 10);
//
//                // Applica l'impulso al centro del corpo del proiettile
//                bodyA.applyLinearImpulse(impulse, bodyA.getWorldCenter(), true);

//                float rotationAngle = (float) Math.atan2(impulseY, impulseX);  // Direzione dell'impulso (angolo)
//
//                // Imposta la rotazione del corpo in modo che segua l'angolo della direzione
//                bodyA.setTransform(bodyA.getPosition(), rotationAngle);  // Setta la rotazione basata sull'angolo calcolato
//                actorA.angle = (float)(rotationAngle);
//
//                bodyA.setFixedRotation(true);

                // Blocca la rotazione successiva, ma la rotazione sarà in linea con la deviazione
//            }

//        }
    }

}