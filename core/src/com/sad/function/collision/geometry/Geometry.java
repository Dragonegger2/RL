package com.sad.function.collision.geometry;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.Epsilon;

public class Geometry {
    public static final float getRotationRadius(Vector2... vertices) {
        return Geometry.getRotationRadius(new Vector2(), vertices);
    }

    public static float getRotationRadius(Vector2 center, Vector2[] vertices) {
        if (vertices == null) return 0.0f;
        if (center == null) center = new Vector2();

        int size = vertices.length;
        if (size == 0) return 0.0f;

        float r2 = 0.0f;
        for (int i = 0; i < size; i++) {
            Vector2 v = vertices[i];
            if (v == null) continue;
            float r2t = v.cpy().sub(center).len2();
            r2 = Math.max(r2, r2t);
        }

        return (float) Math.sqrt(r2);
    }

    public static Vector2[] getCounterClockwiseEdgeNormals(Vector2[] vertices) {
        if (vertices == null) return null;

        int size = vertices.length;
        if (size == 0) return null;

        Vector2[] normals = new Vector2[size];
        for (int i = 0; i < size; i++) {
            // get the edge points
            Vector2 p1 = vertices[i];
            Vector2 p2 = (i + 1 == size) ? vertices[0] : vertices[i + 1];
            // create the edge and get its left perpedicular vector
            Vector2 n = left(p2.cpy().sub(p1)).nor();
            // normalize it
            normals[i] = n;
        }

        return normals;
    }

    /**
     * Get the left hand normal of the provided vector.
     *
     * @param v
     * @return
     */
    public static Vector2 left(Vector2 v) {
        return new Vector2(v.y, -v.x);
    }

    public static Vector2 project(Vector2 a, Vector2 b) {
        float dotProd = a.dot(b);
        float denominator = b.dot(b);
        if (denominator <= Epsilon.E) return new Vector2();
        denominator = dotProd / denominator;
        return new Vector2(denominator * b.x, denominator * b.y);
    }
}