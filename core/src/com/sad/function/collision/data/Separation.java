package com.sad.function.collision.data;

import com.badlogic.gdx.math.Vector2;

public class Separation {
    protected Vector2 normal;
    protected float distance;
    protected Vector2 point1;
    protected Vector2 point2;

    public Separation() {
    }

    /**
     * Full constructor
     *
     * @param normal   penetration normal
     * @param distance distance
     * @param point1   closest point on the first convex shape to the second.
     * @param point2   closest point on the second convex shape to the first.
     */
    public Separation(Vector2 normal, float distance, Vector2 point1, Vector2 point2) {
        this.normal = normal;
        this.distance = distance;
        this.point1 = point1;
        this.point2 = point2;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public void setNormal(Vector2 normal) {
        this.normal = normal;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Vector2 getPoint1() {
        return point1;
    }

    public void setPoint1(Vector2 point1) {
        this.point1 = point1;
    }

    public Vector2 getPoint2() {
        return point2;
    }

    public void setPoint2(Vector2 point2) {
        this.point2 = point2;
    }

    public void clear() {
        this.normal = null;
        this.distance = 0;
        this.point1 = null;
        this.point2 = null;
    }


}
