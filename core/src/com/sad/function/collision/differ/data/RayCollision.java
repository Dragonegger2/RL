package com.sad.function.collision.differ.data;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.differ.shapes.Ray;
import com.sad.function.collision.differ.shapes.Shape;

public class RayCollision {
    public Shape shape;
    public Ray ray;

    public float start = 0.0f;
    public float end = 0.0f;

    public RayCollision reset() {
        ray = null;
        shape = null;
        start = 0.0f;
        end = 0.0f;

        return this;
    }

    public Vector2 collisionPoint() {
        return ray.start.cpy().add(ray.getDir().cpy().scl(start));
    }

    public RayCollision clone() {
        RayCollision _clone = new RayCollision();
        _clone.copy(this);
        return _clone;
    }
    public void copy(RayCollision other) {
        ray = other.ray;
        shape = other.shape;
        start = other.start;
        end = other.end;
    }

}
