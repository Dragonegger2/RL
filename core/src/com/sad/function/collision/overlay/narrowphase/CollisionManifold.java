package com.sad.function.collision.overlay.narrowphase;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;

public class CollisionManifold {
    public Vector2 normal;
    public float distance;
    public Body body1;
    public BodyFixture bodyFixture1;
    public Body body2;
    public BodyFixture bodyFixture2;

    public CollisionManifold(){}

    public CollisionManifold(Vector2 normal, float distance, Body body1, BodyFixture bodyFixture1, Body body2, BodyFixture bodyFixture2) {
        this.normal = normal;
        this.distance = distance;
        this.body1 = body1;
        this.bodyFixture1 = bodyFixture1;
        this.body2 = body2;
        this.bodyFixture2 = bodyFixture2;
    }

    public void clear() {
        normal = null;
        distance = 0;
        body1 = null;
        body2 = null;
        bodyFixture1 = null;
        bodyFixture2 = null;
    }
}