package com.sad.function.collision;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Convex;

public class MinkowskiSum {
    final Convex convex1;
    final Convex convex2;

    final Transform transform1;
    final Transform transform2;

    public MinkowskiSum(Convex convex1, Transform transform1, Convex convex2, Transform transform2) {
        this.convex1 = convex1;
        this.convex2 = convex2;
        this.transform1 = transform1;
        this.transform2 = transform2;
    }

    public final Vector2 getSupportPoint(Vector2 direction) {
        Vector2 point1 = convex1.getFarthestPoint(direction, transform1);
        direction.scl(-1);
        Vector2 point2 = convex2.getFarthestPoint(direction, transform2);
        direction.scl(-1);

        return point1.sub(point2);
    }

    public Convex getConvex1() { return convex1; }
    public Convex getConvex2() { return convex2; }

    public Transform getTransform1() { return transform1; }
    public Transform getTransform2() { return transform2; }
}
