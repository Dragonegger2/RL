package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.geometry.Geometry;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.geometry.Mass;

public class Rectangle extends Polygon {
    private final float width;
    private final float height;

    public Rectangle(float width, float height, Vector2[] vertices) {
        super(new Vector2(), vertices[0].len(), vertices, new Vector2[] {
                new Vector2(0.0f, -1.0f),
                new Vector2(1.0f, 0.0f),
                new Vector2(0.0f, 1.0f),
                new Vector2(-1.0f, 0.0f)
        });

        this.width = width;
        this.height = height;
    }

    public Rectangle(float width, float height) {
        this(width, height, new Vector2[] {
                new Vector2(-width * 0.5f, -height * 0.5f),
                new Vector2( width * 0.5f, -height * 0.5f),
                new Vector2( width * 0.5f,  height * 0.5f),
                new Vector2(-width * 0.5f,  height * 0.5f)
        });
    }

    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }

    @Override
    public Vector2[] getAxes(Vector2[] foci, Transform transform) {
        // get the number of foci
        int fociSize = foci != null ? foci.length : 0;
        // create an array to hold the axes
        Vector2[] axes = new Vector2[2 + fociSize];
        int n = 0;
        // return the normals to the surfaces, since this is a
        // rectangle we only have two axes to test against
        axes[n++] = transform.getTransformedR(this.normals[1]);
        axes[n++] = transform.getTransformedR(this.normals[2]);
        // get the closest point to each focus
        for (int i = 0; i < fociSize; i++) {
            // get the current focus
            Vector2 focus = foci[i];
            // create a place for the closest point
            Vector2 closest = transform.getTransformed(this.vertices[0]);
            float d = closest.cpy().sub(focus).len2();
            // find the minimum distance vertex
            for (int j = 1; j < 4; j++) {
                // get the vertex
                Vector2 vertex = this.vertices[j];
                // transform it into world space
                vertex = transform.getTransformed(vertex);
                // get the squared distance to the focus
                float dt = vertex.cpy().sub(focus).len2();//focus.cpy().sub(vertex).len2();
                // compare with the last distance
                if (dt < d) {
                    // if its closer then save it
                    closest = vertex;
                    d = dt;
                }
            }
            // once we have found the closest point create
            // a vector from the focal point to the point
            Vector2 axis = closest.cpy().sub(focus);
            // normalize the axis
            axis.nor();
            // add it to the array
            axes[n++] = axis;
        }
        // return all the axes
        return axes;
    }

    @Override
    public boolean contains(Vector2 point, Transform transform) {
        // put the point in local coordinates
        Vector2 p = transform.getInverseTransformed(point);
        // get the center and vertices
        Vector2 c = this.center;
        Vector2 p1 = this.vertices[0];
        Vector2 p2 = this.vertices[1];
        Vector2 p4 = this.vertices[3];
        // get the width and height squared
        float widthSquared = p2.cpy().sub(p1).len2();
        float heightSquared = p4.cpy().sub(p1).len2();
        // i could call the polygon one instead of this method, but im not sure which is faster
        Vector2 projectAxis0 = p2.cpy().sub(p1);
        Vector2 projectAxis1 = p4.cpy().sub(p1);
        // create a vector from the centroid to the point
        Vector2 toPoint = p.cpy().sub(c);
        // find the projection of this vector onto the vector from the
        // centroid to the edge
        if (Geometry.project(toPoint, projectAxis0).len2() <= (widthSquared * 0.25)) {
            // if the projection of the v vector onto the x separating axis is
            // smaller than the half width then we know that the point is within the
            // x bounds of the rectangle
            if (Geometry.project(toPoint, projectAxis1).len2() <= (heightSquared * 0.25)) {
                // if the projection of the v vector onto the y separating axis is
                // smaller than the half height then we know that the point is within
                // the y bounds of the rectangle
                return true;
            }
        }
        // return null if they do not intersect
        return false;
    }

    @Override
    public Mass createMass(float density) {
        float h = this.height;
        float w = this.width;

        float mass = density * h * w;

        float inertia = mass * (h * h + w * w) / 12.0f;
        return new Mass(this.center, mass, inertia);
    }
}
