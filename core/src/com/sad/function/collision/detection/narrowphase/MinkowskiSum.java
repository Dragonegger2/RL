package com.sad.function.collision.detection.narrowphase;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Convex;

public class MinkowskiSum {
    final Convex c1;
    final Convex c2;
    final Transform t1;
    final Transform t2;

    public MinkowskiSum(Convex convex1, Transform transform1, Convex convex2, Transform transform2) {
        this.c1 = convex1;
        this.c2 = convex2;
        this.t1 = transform1;
        this.t2 = transform2;
    }

    public Convex getConvex1() {
        return c1;
    }

    public Convex getConvex2() {
        return c2;
    }

    public Transform getTransform1() {
        return t1;
    }

    public Transform getTransform2() {
        return t2;
    }

    public final Vector2 getSupportPoint(Vector2 direction) {
        Vector2 point1 = c1.getFarthestPoint(direction, t1);
        direction.scl(-1);
        Vector2 point2 = c2.getFarthestPoint(direction, t2);
        direction.scl(-1);
        return new Vector2(point1.x - point2.x, point1.y - point2.y);
    }

    public final MinkowskiSumPoint getSupportPoints(Vector2 d) {
        Vector2 point1 = c1.getFarthestPoint(d, t1);
        d.scl(-1);
        Vector2 point2 = c2.getFarthestPoint(d, t2);
        d.scl(-1);
        return new MinkowskiSumPoint(point1, point2);
    }

    public class MinkowskiSumPoint {
        final Vector2 supportPoint1;
        final Vector2 supportPoint2;
        final Vector2 point;

        public MinkowskiSumPoint(Vector2 supportPoint1, Vector2 supportPoint2) {
            this.supportPoint1 = supportPoint1;
            this.supportPoint2 = supportPoint2;
            this.point = new Vector2(supportPoint1.x - supportPoint2.x, supportPoint1.y - supportPoint2.y);
        }

        public Vector2 getSupportPoint1() {
            return supportPoint1;
        }

        public Vector2 getSupportPoint2() {
            return supportPoint2;
        }

        public Vector2 getPoint() {
            return point;
        }
    }
}
