package com.sad.function.system.cd;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sad.function.components.UserData;
import com.sad.function.global.GameInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.global.GameInfo.FIXTURE_TYPE.FOOT;

public class MyContactListener implements ContactListener {
    private static final Logger logger = LogManager.getLogger(MyContactListener.class);

    private String bodyAUserData;
    private String bodyBUserData;

    @Override
    public void beginContact(Contact contact) {
        logger.info("Beginning contact between {} & {}", contact.getFixtureA().getBody().getUserData(), contact.getFixtureB().getBody().getUserData());

        bodyAUserData = (String) contact.getFixtureA().getUserData();
        bodyBUserData = (String) contact.getFixtureB().getUserData();

        handleBeginContact(bodyAUserData, bodyBUserData);
    }

    @Override
    public void endContact(Contact contact) {
        logger.info("Ending contact between {} & {}", contact.getFixtureA().getBody().getUserData(), contact.getFixtureB().getBody().getUserData());

        bodyAUserData = (String) contact.getFixtureA().getUserData();
        bodyBUserData = (String) contact.getFixtureB().getUserData();

        handleEndContact(bodyAUserData, bodyBUserData);
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }


    private void handleBeginContact(String typeA, String typeB) {
        if ("FOOT".equals(typeA) || "FOOT".equals(typeB)) {
            GameInfo.FOOT_CONTACTS++;
        }

    }

    private void handleEndContact(String typeA, String typeB) {
        if ("FOOT".equals(typeA) || "FOOT".equals(typeB)) {
            GameInfo.FOOT_CONTACTS--;
        }
    }
}
