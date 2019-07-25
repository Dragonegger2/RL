package com.sad.function.collision.detection.broadphase;

import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;

public class BroadphasePair {
    private final Body collidable1;
    private final Fixture fixture1;
    private final Body collidable2;
    private final Fixture fixture2;

    public BroadphasePair(Body collidable1, Fixture fixture1, Body collidable2, Fixture fixture2) {
        this.collidable1 = collidable1;
        this.fixture1 = fixture1;
        this.collidable2 = collidable2;
        this.fixture2 = fixture2;
    }

    public Body getCollidable1() {
        return collidable1;
    }

    public Fixture getFixture1() {
        return fixture1;
    }

    public Body getCollidable2() {
        return collidable2;
    }

    public Fixture getFixture2() {
        return fixture2;
    }
}