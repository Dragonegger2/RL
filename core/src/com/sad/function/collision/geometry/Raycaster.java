package com.sad.function.collision.geometry;

import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Convex;

/**
 * Interface for any class that will handle raycasts.
 */
public interface Raycaster {
    boolean raycast(Ray ray, float maxLength, Convex convex, Transform transform, Raycast raycast);
}
