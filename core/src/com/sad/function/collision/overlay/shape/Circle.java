package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

public class Circle extends Shape {
    private Vector2 origin;
    private float radius;

    public Circle(Vector2 origin, float radius) {
        this.origin = origin;
        this.radius = radius;
    }

    @Override
    public Vector2 getVertex(int i, Vector2 axis) {
        Vector2 norAxis = axis.cpy().nor();

        if(i==0) {
            return new Vector2(origin.x + (-norAxis.y * radius),
                    origin.y + (norAxis.x * radius));
        } else {
            return new Vector2(origin.x + (norAxis.y * radius),
                    origin.y + (-norAxis.x * radius));
        }
    }

    /**
     * A circle has no normals.
     * @return an empty set.
     */
    @Override
    public Vector2[] getAxes() {
        return new Vector2[]{};
    }

    /**
     * A circle has two nodes.
     * @return 2. It's always 2.
     */
    @Override
    public int getNumberOfVertices() {
        return 2;
    }
}