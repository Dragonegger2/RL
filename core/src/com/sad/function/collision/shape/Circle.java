package com.sad.function.collision.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.data.AABB;
import com.sad.function.collision.data.Projection;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.data.VUtils;
import com.sad.function.collision.geometry.Mass;

public class Circle extends AbstractShape implements Convex, Shape {
    private float radius;

    public Circle(float radius) {
        super(radius);
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
     * Returns the radius of this circle.
     * @return float radius.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Retur
     * @param center
     * @return
     */
    @Override
    public float getRadius(Vector2 center) {
        return this.radius + VUtils.distance(center, this.center);
    }

    /**
     * Creates a projection for Separating Axis Theorem {@link com.sad.function.collision.detection.narrowphase.SAT}.
     *
     * @param vector    to project along.
     * @param transform to convert local-coordinates to world coordinates.
     * @return the projection values.
     */
    @Override
    public Projection project(Vector2 vector, Transform transform) {
        Vector2 center = transform.getTransformed(this.center);
        float c = center.dot(vector);

        return new Projection(c - this.radius, c + this.radius);
    }

    @Override
    public boolean contains(Vector2 point, Transform transform) {
        Vector2 v = transform.getTransformed(this.center);

        float radiusSquared = this.radius * this.radius;

        v.sub(point);

        return v.len2() <= radiusSquared;
    }

    @Override
    public AABB createAABB(Transform transform) {
        Vector2 center = transform.getTransformed(this.center);
        return new AABB(center, this.radius);
    }

    @Override
    public Mass createMass(float density) {
        float r2 = this.radius * this.radius;

        float mass = (float) (density * Math.PI * r2); //density * area
        float inertia = mass * r2 * 0.5f;

        return new Mass(this.center, mass, inertia);
    }

    /**
     * Handled differently in the {@link com.sad.function.collision.detection.narrowphase.SAT} algorithm since they have an
     * infinite number of axes.
     *
     * @return null
     */
    @Override
    public Vector2[] getAxes(Vector2[] foci, Transform transform) {
        return null;
    }

    @Override
    public Vector2[] getFoci(Transform transform) {
        Vector2[] foci = new Vector2[1];
        foci[0] = transform.getTransformed(this.center);
        return foci;
    }

    /**
     * Returns the furthest vertex in a given direction.
     *
     * @param direction to find support point in.
     * @return furthest vertex point in a given direction.
     */
    @Override
    public Vector2 getFarthestPoint(Vector2 direction, Transform transform) {
        Vector2 nAxis = direction.cpy().nor();
        Vector2 center = getCenter().cpy();

        center.add(transform.x, transform.y);

        center.x += this.radius * nAxis.x;
        center.y += this.radius * nAxis.y;

        return center;
    }
}
