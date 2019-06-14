package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

public abstract class Shape {
    public abstract Vector2 getVertex(int i, Vector2 axis);
    public abstract Vector2[] getAxes();
    public abstract int getNumberOfVertices();

    /**
     * Returns the vector that is perpendicular to the provided one.
     * @return the perpendicular vector.
     */
    public Vector2 normal(Vector2 v) {
        return new Vector2(-1 * v.y, v.x);
    }
}
