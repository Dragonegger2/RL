package com.sad.function.collision.overlay.manifold;

import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.narrowphase.CollisionManifold;
import com.sad.function.collision.overlay.shape.Convex;

public interface ManifoldSolver {
    boolean getManifold(Penetration penetration, Convex convex1, Transform transform1, Convex convex2, Transform transform2, CollisionManifold manifold);
}
