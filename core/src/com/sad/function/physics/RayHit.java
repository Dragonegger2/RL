package com.sad.function.physics;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.cd.shapes.Shape;

public class RayHit {
    private Vector2 collisionPoint,
            pNormal;
    public float distance;

    private float pDepth;

    public Shape collidedWith;

    public RayHit() {}

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
