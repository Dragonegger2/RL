package com.sad.function.collision.differ.data;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.differ.shapes.Ray;

/**
 * Java implementation of Collision:
 * https://github.com/snowkit/differ/tree/master/differ
 */
public class RayIntersection {
    public Ray ray1;
    public Ray ray2;

    public float u1 = 0.0f;
    public float u2 = 0.0f;

    public RayIntersection reset() {
        ray1 = null;
        ray2 = null;
        u1 = 0.0f;
        u2 = 0.0f;

        return this;
    }

    public void copy(RayIntersection other) {
        ray1 = other.ray1;
        ray2 = other.ray2;
        u1 = other.u1;
        u2 = other.u2;
    }

    public Vector2 getIntersectionPoint() { return ray1.start.cpy().add(ray1.getDir().cpy().scl(u1)); }
    public Vector2 getIntersectionPointRay1() { return ray1.start.cpy().add(ray1.getDir().cpy().scl(u1)); }
    public Vector2 getIntersectionPointRay2() { return ray2.start.cpy().add(ray2.getDir().cpy().scl(u2)); }

    public RayIntersection clone() {
        RayIntersection _clone = new RayIntersection();
        _clone.copy(this);
        return _clone;
    }
}
