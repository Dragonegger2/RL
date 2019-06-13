package com.sad.function.collision.differ.data;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.differ.shapes.Ray;
import com.sad.function.collision.differ.shapes.Shape;

public class RayCollision {
    public Shape shape;
    public Ray ray;

    /**
     * measure distance from the distanceFromStartOfRay of the ray.
     *
     * Using below
     * ray.distanceFromStartOfRay.cpy().add(ray.getDir().cpy().scl(distanceFromStartOfRay));
     * give you the collision point.
     * This may result in collisions that are not treating this ray as a line segment. To ensure that it's within my line segment:
     *
     * float xMin = origin.x < end.x ? origin.x : end.x;
     * float xMax = origin.x < end.x ? end.x : origin.x;
     *
     * float yMin = origin.y < end.y ? origin.y : end.y;
     * float yMax = origin.y < end.y ? end.y : origin.y;
     *
     * if(result.x > xMax || result.x < xMin) {
     *     return false;
     * }
     *
     * if(result.y > yMax || result.y < yMin) {
     *     return false;
     * }
     */

    public float distanceFromStartOfRay = 0.0f;
    public float distanceFromEndOfRay = 0.0f;

    //Penetration deption
    public RayCollision reset() {
        ray = null;
        shape = null;
        distanceFromStartOfRay = 0.0f;
        distanceFromEndOfRay = 0.0f;

        return this;
    }

    public Vector2 collisionPoint() {
        return ray.start.cpy().add(ray.getDir().cpy().scl(distanceFromStartOfRay));
    }

    public RayCollision clone() {
        RayCollision _clone = new RayCollision();
        _clone.copy(this);
        return _clone;
    }
    public void copy(RayCollision other) {
        ray = other.ray;
        shape = other.shape;
        distanceFromStartOfRay = other.distanceFromStartOfRay;
        distanceFromEndOfRay = other.distanceFromEndOfRay;
    }

}
