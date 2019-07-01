package com.sad.function.collision.overlay.geometry;

import com.badlogic.gdx.math.Vector2;

public class Raycast {
    private Vector2 point;
    private Vector2 normal;
    private float distance;

    public Raycast() {
    }

    public Raycast(Vector2 point, Vector2 normal, float distance) {
        this.point = point;
        this.normal = normal;
        this.distance = distance;
    }

    public void clear() {
        this.point = null;
        this.normal = null;
        this.distance = 0;
    }

    /**
     * Returns the hit point.
     *
     * @return {@link Vector2}
     */
    public Vector2 getPoint() {
        return this.point;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }

    public Vector2 getNormal() {
        return this.normal;
    }

    public void setNormal(Vector2 normal) {
        this.normal = normal;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
