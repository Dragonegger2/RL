package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.AABB;
import com.sad.function.collision.overlay.data.Projection;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.geometry.Mass;

import java.util.UUID;

public interface Shape {
    UUID getId();
    Vector2 getCenter();
    float getRadius();
    float getRadius(Vector2 center);

    Projection project(Vector2 vector);
    Projection project(Vector2 vector, Transform transform);

    boolean contains(Vector2 point);
    boolean contains(Vector2 point, Transform transform);

    AABB createAABB();
    AABB createAABB(Transform transform);

    Mass createMass(float density);

    /**
     * Returns the vector that is perpendicular to the provided one.
     * @return the perpendicular vector.
     */
    static Vector2 normal(Vector2 v) {
        return new Vector2(-v.y, v.x);
    }
}
