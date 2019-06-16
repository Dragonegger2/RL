package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

/**
 * All axis are stored in local coordinates.
 */
public class Polygon extends Shape {
    final Vector2[] vertices;
    final Vector2[] normals;

    Polygon(Vector2 center, double radius, Vector2[] vertices, Vector2[] normals) {
        this.vertices = vertices;
        this.normals = normals;
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

    public Vector2[] getAxes(Vector2[] foci, Transform transform) {
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
//            double d = f.distanceSquared(closest);
            float d = f.cpy().sub(closest).len2();
            // find the minimum distance vertex
            for (int j = 1; j < size; j++) {
                // get the vertex
                Vector2 p = this.vertices[j];
                // transform it into world space
                p = transform.getTransformed(p);
                // get the squared distance to the focus
//                double dt = f.distanceSquared(p);
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
            Vector2 axis = f.cpy().sub(closest);//Make sure a -> b
            // normalize it
//            axis.normalize();
            axis.nor();
            // add it to the array
            axes[n++] = axis;
        }
        // return all the axes
        return axes;
    }

    @Override
    public Vector2[] getFoci(Transform transform) {
        return null;
    }

    //    @Override
    public Vector2[] getAxes(Transform transform) {
        Vector2[] axes = new Vector2[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            int j = i + 1 == vertices.length ? 0 : i + 1;

            axes[i] = new Vector2(vertices[i].x - vertices[j].x, vertices[i].y - vertices[j].y);
            axes[i].nor();
            axes[i] = normal(axes[i]);
        }
        return axes;
    }

    @Override
    public int getNumberOfVertices() {
        return vertices.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Polygon[").append(super.toString())
                .append("|Vertices={");
        for (int i = 0; i < this.vertices.length; i++) {
            if (i != 0) sb.append(",");
            sb.append(this.vertices[i]);
        }
        sb.append("}")
                .append("]");
        return sb.toString();
    }

}
