package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Transform;

/**
 * All axis are stored in local coordinates.
 */
public interface Convex extends Shape {

    Vector2[] getAxes(Vector2[] foci, Transform transform);

    Vector2[] getFoci(Transform transform);

    Vector2 getFarthestPoint(Vector2 vector, Transform transform);
}
