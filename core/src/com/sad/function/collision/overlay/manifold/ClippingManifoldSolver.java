package com.sad.function.collision.overlay.manifold;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.narrowphase.CollisionManifold;
import com.sad.function.collision.overlay.shape.Convex;

public class ClippingManifoldSolver implements ManifoldSolver {
    @Override
    public boolean getManifold(Penetration penetration, Convex convex1, Transform transform1, Convex convex2, Transform transform2, CollisionManifold manifold) {
        manifold.clear();

        Vector2 n = penetration.normal;

        return false;
    }
}
