package com.sad.function.physics;

import com.badlogic.gdx.math.Vector2;

public class RayHit {
    private Vector2 collisionPoint,
            pNormal;

    private float pDepth;

    public RayHit(Vector2 collisionPoint, Vector2 penetratingVector) {
        this.collisionPoint = collisionPoint;
        pNormal = penetratingVector.cpy().nor();
        pDepth = penetratingVector.len();
    }

    public RayHit() {
        collisionPoint = new Vector2();
        pNormal = new Vector2();
    }

    public float getpDepth() {
        return pDepth;
    }

    public void setpDepth(float pDepth) {
        this.pDepth = pDepth;
    }

    public Vector2 getpNormal() {
        return pNormal;
    }

    public void setpNormal(Vector2 pNormal) {
        this.pNormal = pNormal;
    }

    public Vector2 getCollisionPoint() {
        return collisionPoint;
    }

    public void setCollisionPoint(Vector2 collisionPoint) {
        this.collisionPoint = collisionPoint;
    }
}
