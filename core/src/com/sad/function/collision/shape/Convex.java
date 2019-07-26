package com.sad.function.collision.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.data.Transform;

/**
 * All axis are stored in local coordinates.
 */
public interface Convex extends Shape {

    Vector2[] getAxes(Vector2[] foci, Transform transform);

    Vector2[] getFoci(Transform transform);

    Vector2 getFarthestPoint(Vector2 vector, Transform transform);

//    EdgeFeature getFarthestFeature(Vector2 vector, TransformComponent transform);
}
