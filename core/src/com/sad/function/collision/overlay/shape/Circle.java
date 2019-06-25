package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Transform;

public class Circle {
    private Vector2 origin;
    private float radius;

    public Circle(Vector2 origin, float radius) {
        this.origin = origin;
        this.radius = radius;
    }

    /**
     * A circle has no normals.
     *
     * @return an empty set.
     */
//    @Override
    public Vector2[] getAxes(Transform t) {
        return new Vector2[]{};
    }

    /**
     * A circle has two nodes.
     *
     * @return 2. It'shape always 2.
     */
//    @Override
    public int getNumberOfVertices() {
        return 2;
    }

    public float getRadius() {
        return radius;
    }

    public Vector2 getCenter() { return origin; }

    /**
     * Returns the furthest vertex in a given direction.
     * @param direction
     * @return furthest vertex point in a given direction.
     */
    public Vector2 getSupportPoint(Vector2 direction, Transform transform) {
        Vector2 nAxis = direction.cpy().nor();
        Vector2 center = getCenter().cpy();

        center.add(transform.x, transform.y);

        center.x += this.radius * nAxis.x;
        center.y += this.radius * nAxis.y;

        return center;
    }
}
