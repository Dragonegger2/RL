package com.sad.function.collision.detection.broadphase;

import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;

public class BroadphaseItem {
    private final Body collidable;
    private final Fixture fixture;

    public BroadphaseItem(Body collidable, Fixture fixture) {
        this.collidable = collidable;
        this.fixture = fixture;
    }

    public Body getCollidable() {
        return collidable;
    }

    public Fixture getFixture() {
        return fixture;
    }
}
