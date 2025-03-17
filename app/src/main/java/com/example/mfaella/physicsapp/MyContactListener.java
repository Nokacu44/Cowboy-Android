package com.example.mfaella.physicsapp;

import android.util.Log;

import com.badlogic.androidgames.framework.Sound;
import com.example.mfaella.physicsapp.actors.Actor;
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
        Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
    }
}