package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.VUtils;
import com.sad.function.collision.overlay.geometry.Geometry;
import com.sad.function.collision.overlay.data.AABB;
import com.sad.function.collision.overlay.data.Projection;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.geometry.Mass;
import org.dyn4j.geometry.EdgeFeature;

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

    private Polygon(Vector2[] vertices, Vector2 center) {
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

//    public EdgeFeature getFarthestFeature(Vector2 vector, Transform transform) {
//        Vector2 localn = transform.getInverseTransformedR(vector);
//        Vector2 maximum = new Vector2();
//        float max = -Float.MAX_VALUE;
//
//        int index = 0;
//        int count = vertices.length;
//
//        for(int i = 0; i < count; i++) {
//            Vector2 v = vertices[i];
//            float projection = localn.dot(v);
//
//            if(projection > max) {
//                maximum.set(v);
//                max = projection;
//
//                index = i;
//            }
//        }
//
//        //Find the most perpendicular edge.
//        int l = index + 1 == count ? 0 : index + 1;
//        int r = index - 1 < 0 ? count - 1 : index - 1;
//        Vector2 leftN = normals[index == 0 ? count - 1 : index - 1];
//        Vector2 rightN = normals[index];
//
//        transform.transform(maximum);
//    }

    @Override
    public AABB createAABB(Transform transform) {
        // get the first point
        Vector2 p = transform.getTransformed(this.vertices[0]);
        // project the point onto the vector
        float minX = p.x;
        float maxX = p.x;
        float minY = p.y;
        float maxY = p.y;
        // loop over the rest of the vertices
        int size = this.vertices.length;
        for(int i = 1; i < size; i++) {
            // get the next point
            p = transform.getTransformed(this.vertices[i]);
            // project it onto the vector
            float vx = p.x;
            float vy = p.y;
            // compare the x values
            if (vx < minX) {
                minX = vx;
            } else if (vx > maxX) {
                maxX = vx;
            }
            // compare the y values
            if (vy < minY) {
                minY = vy;
            } else if (vy > maxY) {
                maxY = vy;
            }
        }
        // create the aabb
        return new AABB(minX, minY, maxX, maxY);
    }

    @Override
    public Mass createMass(float density) {
        // can't use normal centroid calculation since it will be weighted towards sides
        // that have larger distribution of points.

        Vector2 center = new Vector2();
        float area = 0.0f;
        float I = 0.0f;
        int n = this.vertices.length;
        // get the average center
        Vector2 ac = new Vector2();
        for (int i = 0; i < n; i++) {
            ac.add(this.vertices[i]);
        }
        ac.scl(1.0f / n);
        // loop through the vertices
        for (int i = 0; i < n; i++) {
            // get two vertices
            Vector2 p1 = this.vertices[i];
            Vector2 p2 = i + 1 < n ? this.vertices[i + 1] : this.vertices[0];
            // get the vector from the center to the point
            p1 = p1.cpy().sub(ac);
            p2 = p2.cpy().sub(ac);
            // perform the cross product (yi * x(i+1) - y(i+1) * xi)
            float D = VUtils.cross(p1, p2);
            // multiply by half
            float triangleArea = 0.5f * D;
            // add it to the total area
            area += triangleArea;

            // area weighted centroid
            // (p1 + p2) * (D / 6)
            // = (x1 + x2) * (yi * x(i+1) - y(i+1) * xi) / 6
            // we will divide by the total area later

            center.x += (p1.x + p2.x) * (1.0f / 3.0f) * triangleArea;
            center.y += (p1.y + p2.y) * (1.0f / 3.0f) * triangleArea;

            // (yi * x(i+1) - y(i+1) * xi) * (p2^2 + p2 . p1 + p1^2)
            I += triangleArea * (p2.dot(p2) + p2.dot(p1) + p1.dot(p1));
            // we will do the m / 6A = (d / 6) when we have the area summed up
        }
        // compute the mass
        float m = density * area;
        // finish the centroid calculation by dividing by the total area
        // and adding in the average center
        center.scl(1.0f / area);
        Vector2 c = center.add(ac);
        // finish the inertia tensor by dividing by the total area and multiplying by d / 6
        I *= (density / 6.0);
        // shift the axis of rotation to the area weighted center
        // (center is the vector from the average center to the area weighted center since
        // the average center is used as the origin)
        I -= m * center.len2();

        return new Mass(c, m, I);    }
}
