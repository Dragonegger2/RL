package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.Geometry;
import com.sad.function.collision.overlay.data.Projection;
import com.sad.function.collision.overlay.data.Transform;

/**
 * All axis are stored in local coordinates.
 */
public class Polygon extends AbstractShape implements Convex, Shape {
    final Vector2[] vertices;
    final Vector2[] normals;

    Polygon(Vector2 center, float radius, Vector2[] vertices, Vector2[] normals) {
        super(center, radius);
        this.vertices = vertices;
        this.normals = normals;
    }

    Polygon(Vector2[] vertices, Vector2 center) {
        super(center, Geometry.getRotationRadius(center, vertices));

        this.vertices = vertices;
        this.normals = Geometry.getCounterClockwiseEdgeNormals(vertices);
    }

    @Override
    public Vector2[] getVertices() { return this.vertices; }

    @Override
    public Vector2[] getNormals() {
        return this.normals;
    }

    @Override
    public float getRadius(Vector2 center) {
        return Geometry.getRotationRadius(center, this.vertices);
    }

    @Override
    public Vector2[] getAxes(Vector2[] foci, Transform transform) {
        // get the size of the foci list
        int fociSize = foci != null ? foci.length : 0;
        // get the number of vertices this polygon has
        int size = this.vertices.length;
        // the axes of a polygon are created from the normal of the edges
        // plus the closest point to each focus
        Vector2[] axes = new Vector2[size + fociSize];
        int n = 0;
        // loop over the edge normals and put them into world space
        for (int i = 0; i < size; i++) {
            // create references to the current points
            Vector2 v = this.normals[i];
            // transform it into world space and add it to the list
            axes[n++] = transform.getTransformedR(v);
        }
        // loop over the focal points and find the closest
        // points on the polygon to the focal points
        for (int i = 0; i < fociSize; i++) {
            // get the current focus
            Vector2 f = foci[i];
            // create a place for the closest point
            Vector2 closest = transform.getTransformed(this.vertices[0]);

            float d = f.cpy().sub(closest).len2();
            // find the minimum distance vertex
            for (int j = 1; j < size; j++) {
                // get the vertex
                Vector2 p = this.vertices[j];
                // transform it into world space
                p = transform.getTransformed(p);
                // get the squared distance to the focus
                float dt = f.cpy().sub(p).len2();
                // compare with the last distance
                if (dt < d) {
                    // if its closer then save it
                    closest = p;
                    d = dt;
                }
            }
            // once we have found the closest point create
            // a vector from the focal point to the point
            Vector2 axis = closest.cpy().sub(f);
            // normalize it
            axis.nor();
            // add it to the array
            axes[n++] = axis;
        }
        // return all the axes
        return axes;
    }

    @Override
    public Projection project(Vector2 vector, Transform transform) {
        float v = 0.0f;
        Vector2 p = transform.getTransformed(this.vertices[0]);

        float min = vector.dot(p);
        float max = min;

        int size = this.vertices.length;
        for(int i = 0; i < size; i++) {
            p = transform.getTransformed(this.vertices[i]);
            v = vector.dot(p);
            if(v < min) {
                min = v;
            } else if (v > max) {
                max = v;
            }
        }
        return new Projection(min, max);
    }

    @Override
    public boolean contains(Vector2 point, Transform transform) {
        return false;
    }

    @Override
    public void translate(float x, float y) {
        super.translate(x, y);
        int size = this.vertices.length;
        for(int i = 0; i < size; i++) {
            this.vertices[i].add(x, y);
        }
    }

    /**
     * Not pertinent to this shape.
     * @param transform to calculate the new points for this shape. Not necessary.
     * @return null
     */
    @Override
    public Vector2[] getFoci(Transform transform) {
        return null;
    }

    @Override
    public Vector2 getFarthestPoint(Vector2 vector, Transform transform) {
        // transform the normal into local space
        Vector2 localn = transform.getInverseTransformedR(vector);

        // set the farthest point to the first one
        int index = 0;
        // prime the projection amount
        double max = localn.dot(this.vertices[0]);
        // loop through the rest of the vertices to find a further point along the axis
        int size = this.vertices.length;
        for (int i = 1; i < size; i++) {
            // get the current vertex
            Vector2 v = this.vertices[i];
            // project the vertex onto the axis
            double projection = localn.dot(v);
            // check to see if the projection is greater than the last
            if (projection > max) {
                // otherwise this point is the farthest so far so clear the array and add it
                index = i;
                // set the new maximum
                max = projection;
            }
        }

        // transform the point into world space and return
        return transform.getTransformed(this.vertices[index]);
    }


}
