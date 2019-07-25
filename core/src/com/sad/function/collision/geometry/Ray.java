package com.sad.function.collision.geometry;

import com.badlogic.gdx.math.Vector2;

public class Ray {
    private Vector2 start;
    private Vector2 direction;

    /**
     * Create a new ray that is cast from the origin in a {@param direction}.
     *
     * @param direction the ray is cast.
     */
    public Ray(Vector2 direction) {
        this(new Vector2(), direction);
    }

    /**
     * Full constructor.
     *
     * @param start     point of the ray.
     * @param direction of the ray. Normalized.
     */
    public Ray(Vector2 start, Vector2 direction) {
        this.start = start;
        this.direction = new Vector2(direction.x, direction.y).nor();
    }

    public Vector2 getStart() {
        return start;
    }

    public void setStart(Vector2 start) {
        this.start = start;
    }

    public Vector2 getDirectionVector() {
        return direction;
    }

    public float getDirection() {
        return (float) Math.atan2(direction.x, direction.y);
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }
}
