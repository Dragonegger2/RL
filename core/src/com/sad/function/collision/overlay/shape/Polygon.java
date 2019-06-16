package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Interval;

/**
 * All axis are stored in local coordinates.
 */
public class Polygon extends AbstractShape {
    Vector2 origin; //TODO: Add a method to calculate the origin.
    final Vector2[] vertices;

    Polygon(Vector2 center, float radius, Vector2[] vertices) {
        super(center, radius);

        this.vertices = vertices;
    }

    private Polygon(Vector2[] vertices, Vector2 center) {
        super(center, 0);

        this.vertices = vertices;
    }

    @Override
    public Vector2 getVertex(int i, Transform t, Vector2 axis) {
        return null;
    }

    @Override
    public Vector2[] getAxes(Transform transform) {
        return new Vector2[0];
    }

    @Override
    public int getNumberOfVertices() {
        return 0;
    }

    @Override
    public Projection project(Vector2 vector, Transform transform) {
        float v;
        Vector2 p = transform.getTransformed(this.vertices[0]);
        float min = vector.dot(p);
        float max = min;
        int size = this.vertices.length; //TODO Need to have some sort of check for vertices
        for(int i = 0; i < size; i++) {
            p = transform.getTransformed(this.vertices[i]);
            v = vector.dot(p);

            if(v < min) {
                min = v;
            } else if ( v> max ) {
                max = v;
            }
        }
        return new Projection(min, max);
    }
}
