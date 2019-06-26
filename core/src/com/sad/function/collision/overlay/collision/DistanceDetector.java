package com.sad.function.collision.overlay.collision;

import com.sad.function.collision.overlay.data.Separation;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.Convex;

public interface DistanceDetector {
    boolean distance(Convex c1, Transform t1, Convex c2, Transform t2, Separation separation);
}
