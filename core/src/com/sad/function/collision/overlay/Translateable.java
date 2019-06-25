package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;

/**
 * Object that can be moved about the X-Y plane.
 */
public interface Translateable {
    /**
     * Move the object in given amounts in their respective directions.
     * @param x change in the x direction.
     * @param y change in the y direction.
     */
    void translate(float x, float y);

    /**
     * Vector2 version of the other translate method.
     * @param translation
     */
    void translate(Vector2 translation);
}
