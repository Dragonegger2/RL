package com.sad.function.collision.detection;

import com.sad.function.collision.data.Separation;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Convex;

public interface DistanceDetector {
    boolean distance(Convex c1, Transform t1, Convex c2, Transform t2, Separation separation);
}
