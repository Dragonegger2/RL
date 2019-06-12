package com.sad.function.physics;

import com.badlogic.gdx.math.Vector2;

public class Ray {
    //Direction should be a normalized vector.
    private Vector2 direction;
    private Vector2 origin;

    public Ray() {
        origin = new Vector2();
        direction = new Vector2();
    }

    public Ray setDirection(Vector2 direction) {
        this.direction = direction.cpy();
        return this;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public Ray setOrigin(Vector2 origin) {
        this.origin = origin.cpy();
        return this;
    }

    public Ray setOrigin(float x, float y) {
        this.origin = new Vector2(x, y);
        return this;
    }

    /**
     * Returns a point in space along the ray that is the provided distance away.
     * @param distance to cast the ray.
     * @return point along the ray distance away from the origin.
     */
    public Vector2 cast(float distance) {
        return origin.cpy().add(direction.cpy().scl(distance));
    }
    public Vector2 getDirection() {
        return direction;
    }
}
