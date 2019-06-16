package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

public class Transform {
    public static final Transform IDENTITY = null;
    public float x = 0;
    public float y = 0;

    public Transform() {}
    public Transform(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Transform(Vector2 xy) {
        this.x = xy.x;
        this.y = xy.y;
    }

    /**
     * Atomically safe, does not modify the source.
     * @param v
     * @return
     */
    public Vector2 getTransformed(Vector2 v) {
        return v.cpy().add(x, y);
    }
}
