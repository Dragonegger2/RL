package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

public class Polygon extends Shape {
    Vector2 origin; //TODO: Add a method to calculate the origin.
    Vector2[] nodes;

    public Polygon(Vector2[] vertexes, boolean centered) {
        nodes = new Vector2[vertexes.length];

        for (int i = 0; i < vertexes.length; i++) {
            nodes[i] = vertexes[i].cpy();
        }
    }

    /**
     * Calculate the area of a polygon.
     *
     * @param polyPoints points of a polygon
     * @return area of the polygon.
     */
    private static float area(Vector2[] polyPoints) {
        int i, j, n = polyPoints.length;
        float area = 0;

        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            area += polyPoints[i].x * polyPoints[j].y;
            area -= polyPoints[j].x * polyPoints[i].y;
        }
        area /= 2.0;
        return (area);
    }

    /**
     * Calculate center of mass for the polygon.
     *
     * @param polyPoints to calculate the center with.
     * @return the center/origin of the polygon.
     */
    private static Vector2 centerOfMass(Vector2[] polyPoints) {
        float cx = 0, cy = 0;
        float area = area(polyPoints);
        // could change this to Point2D.Float if you want to use less memory
        Vector2 res = new Vector2();
        int i, j, n = polyPoints.length;

        double factor = 0;
        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            factor = (polyPoints[i].x * polyPoints[j].y
                    - polyPoints[j].x * polyPoints[i].y);
            cx += (polyPoints[i].x + polyPoints[j].x) * factor;
            cy += (polyPoints[i].y + polyPoints[j].y) * factor;
        }
        area *= 6.0f;
        factor = 1 / area;
        cx *= factor;
        cy *= factor;
        res.set(cx, cy);
        return res;
    }

    @Override
    public Vector2 getVertex(int i, Vector2 axis) {
        return nodes[i];
    }

    @Override

    public Vector2[] getAxes() {
        Vector2[] axes = new Vector2[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            int j = i + 1 == nodes.length ? 0 : i + 1;

            axes[i] = new Vector2(nodes[i].x - nodes[j].x, nodes[i].y - nodes[j].y);
            axes[i].nor();
            axes[i] = normal(axes[i]);
        }
        return axes;
    }

    @Override
    public int getNumberOfVertices() {
        return nodes.length;
    }
}
