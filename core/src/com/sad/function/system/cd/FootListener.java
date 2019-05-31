package com.sad.function.system.cd;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FootListener implements ContactListener {
    private static final Logger logger = LogManager.getLogger(FootListener.class);

    @Override
    public void beginContact(Contact contact) {
//        Object fixtureUserData = contact.getFixtureA().getUserData();
//        if(((UserData)fixtureUserData).type == UserData.ObjectType.GROUND ) {
//            player.numFootContacts++;
//        }

        logger.info("Beginning contact between {} & {}", contact.getFixtureA(), contact.getFixtureB());
    }

    @Override
    public void endContact(Contact contact) {
//        Object fixtureUserData = contact.getFixtureA().getUserData();
//        if (((UserData)fixtureUserData).type == UserData.ObjectType.GROUND) {
//            player.numFootContacts--;
//        }
//
        logger.info("Ending contact between {} & {}", contact.getFixtureA(), contact.getFixtureB());

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
