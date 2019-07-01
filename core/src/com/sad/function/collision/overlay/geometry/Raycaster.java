package com.sad.function.collision.overlay.geometry;

import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.Convex;

/**
 * Interface for any class that will handle raycasts.
 */
public interface Raycaster {
    boolean raycast(Ray ray, float maxLength, Convex convex, Transform transform, Raycast raycast);
}
