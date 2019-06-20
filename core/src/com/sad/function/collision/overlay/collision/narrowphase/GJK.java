package com.sad.function.collision.overlay.collision.narrowphase;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.collision.VUtils;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Separation;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.Circle;
import com.sad.function.collision.overlay.shape.Convex;
import org.dyn4j.Epsilon;

import java.util.ArrayList;
import java.util.List;

public class GJK implements NarrowPhaseDetector, DistanceDetector {
    public static final int DEFAULT_MAX_ITERATIONS = 30;
    public static final float DEFAULT_DETECT_EPSILON = 0.0f;
    public static final float DEFAULT_DISTANCE_EPSILON = (float) Math.sqrt(Epsilon.E);

    private static final Vector2 ORIGIN = new Vector2(0, 0);

    protected int maxDistanceIterations = GJK.DEFAULT_MAX_ITERATIONS;
    protected float detectEpsilon = GJK.DEFAULT_DETECT_EPSILON;
    protected float distanceEpsilon = GJK.DEFAULT_DISTANCE_EPSILON;

    protected EPA epa = new EPA();

    public static Vector2 getInitialDirection(Convex c1, Transform t1, Convex c2, Transform t2) {
        Vector2 tC1O = t1.getTransformed(c1.getCenter());
        Vector2 tC2O = t2.getTransformed(c2.getCenter());

        return tC2O.sub(tC1O);
    }

    public static Vector2 getSupportPoint(Convex c1, Transform t1, Convex c2, Transform t2, Vector2 d) {
        Vector2 point1 = c1.getFarthestPoint(d, t1);
        d.scl(-1);      //Flip search direction
        Vector2 point2 = c2.getFarthestPoint(d, t2);
        d.scl(-1);      //Return search direction to original orientation.

        return point2.cpy().sub(point1); //TODO Verify this damn thing again.
    }

    public static boolean containsOrigin(Vector2 a, Vector2 b, Vector2 c) {
        float sa = VUtils.cross(a, b);
        float sb = VUtils.cross(b, c);
        float sc = VUtils.cross(c, a);

        return (sa * sb > 0 && sa * sc > 0);
    }

    @Override
    public boolean detect(Convex c1, Transform t1, Convex c2, Transform t2, Penetration penetration) {
        if (c1 instanceof Circle && c2 instanceof Circle) {
            return false; //TODO: Circle calculations.
        }

        List<Vector2> simplex = new ArrayList<>(3);

        //Find the initial search direction and populate the simplex with it; prevents the 0th case of the GJK alg.
        Vector2 d = getInitialDirection(c1, t1, c2, t2);
        if (d.isZero()) d.set(1, 0);

        if (detect(c1, t1, c2, t2, simplex, d)) {

            epa.getPenetration(simplex, c1, t1, c2, t2, penetration);
            return true;
        }

        return false;
    }

    @Override
    public boolean detect(Convex c1, Transform t1, Convex c2, Transform t2) {
        if (c1 instanceof Circle && c2 instanceof Circle) {
            return false; //TODO Needs to be implemented.
        }

        List<Vector2> simplex = new ArrayList<>(3);
        Vector2 d = getInitialDirection(c1, t1, c2, t2);

        return detect(c1, t1, c2, t2, simplex, d);
    }

    protected boolean detect(Convex c1, Transform t1, Convex c2, Transform t2, List<Vector2> simplex, Vector2 d) {
        if (d.isZero()) d.set(1, 0);

        simplex.add(getSupportPoint(c1, t1, c2, t2, d));
        if (simplex.get(0).dot(d) <= 0) {
            return false;
        }

        //flip initial search direction since we have already added a point that is furthest in that direction.
        d.scl(-1);

        for (int i = 0; i < DEFAULT_MAX_ITERATIONS; i++) {
            Vector2 support = getSupportPoint(c1, t1, c2, t2, d);
            simplex.add(support);

            if (support.dot(d) <= Epsilon.E) {
                return false;
            } else {
                if (checkSimplex(simplex, d)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkSimplex(List<Vector2> simplex, Vector2 d) {
        Vector2 a = simplex.get(simplex.size() - 1);
        Vector2 ao = a.cpy().scl(-1);//Flip a.

        //In 2D we are trying to construct a triangle; the minimum number of data points we need to encapsulate a single point.
        //In our case we are trying to encapsulate the origin (0,0).
        if (simplex.size() == 3) {
            Vector2 b = simplex.get(1);
            Vector2 c = simplex.get(0);

            //edges
            Vector2 ab = b.cpy().sub(a);
            Vector2 ac = c.cpy().sub(a);

            Vector2 acPerp = new Vector2();
            float dot = ab.x * ac.y - ac.x * ab.y;
            acPerp.x = -ac.y * dot;
            acPerp.y = ac.x * dot;

            float acLocation = acPerp.dot(ao);
            if (acLocation >= 0.0f) {
                simplex.remove(1);
                d.set(acPerp);
            } else {
                Vector2 abPerp = new Vector2();
                abPerp.x = ab.y * dot;
                abPerp.y = -ab.x * dot;

                float abLocation = abPerp.dot(ao);
                if (abLocation < 0.0f) {
                    return true;
                } else {
                    simplex.remove(0);
                    d.set(abPerp);
                }
            }
        } else {
            Vector2 b = simplex.get(0);
            Vector2 ab = b.cpy().sub(a);

            //TODO Triple product.

            if (d.len2() <= Epsilon.E) {
                d.set(VUtils.left(ab));
            }
        }
        return false;
    }

    @Override
    public boolean distance(Convex convex1, Transform t1, Convex convex2, Transform t2, Separation separation) {
        //TODO Circle-to-Circle
        MinkowskiSum ms = new MinkowskiSum(convex1, t1, convex2, t2);

        MinkowskiSum.MinkowskiSumPoint a = null;
        MinkowskiSum.MinkowskiSumPoint b = null;
        MinkowskiSum.MinkowskiSumPoint c = null;

        Vector2 c1 = t1.getTransformed(convex1.getCenter());
        Vector2 c2 = t2.getTransformed(convex2.getCenter());

        Vector2 d = c2.cpy().sub(c1);

        if (d.isZero()) return false;

        a = ms.getSupportPoints(d);
        d.scl(-1);
        b = ms.getSupportPoints(d);

        d = Segment.getPointOnSegmentClosestToPoint(ORIGIN, b.point, a.point);
        for (int i = 0; i < 30; i++) {
            d.scl(-1);
            if (d.len2() <= Epsilon.E) {
                return false;
            }

            c = ms.getSupportPoints(d);

            if (containsOrigin(a.point, b.point, c.point)) {
                return false;
            }

            float projection = c.point.dot(d);
            if ((projection - a.point.dot(d)) < this.distanceEpsilon) {
                d.nor();
                separation.setNormal(d);
                separation.setDistance(-c.point.dot(d));
                findClosestPoints(a, b, separation);
                return true;
            }

            Vector2 p1 = Segment.getPointOnSegmentClosestToPoint(ORIGIN, a.point, c.point);
            Vector2 p2 = Segment.getPointOnSegmentClosestToPoint(ORIGIN, c.point, b.point);

            float p1Magnitude = p1.len2();
            float p2Magnitude = p2.len2();

            if(p1Magnitude <= Epsilon.E) {
                d.nor();
                separation.setDistance(p1.len());
                p1.nor();
                separation.setNormal(d);
                this.findClosestPoints(a, c, separation);
                return true;
            } else if( p2Magnitude <= Epsilon.E) {
                d.nor();
                separation.setDistance(p2.len());
                p2.nor();
                separation.setNormal(d);
                this.findClosestPoints(c, b, separation);
                return true;
            }

            if(p1Magnitude < p2Magnitude) {
                b = c;
                d = p1;
            } else {
                a = c;
                d = p2;
            }

        }

        d.nor();
        separation.setNormal(d);
        separation.setDistance(-c.point.dot(d));
        this.findClosestPoints(a, b, separation);

        return true;
    }

    private void findClosestPoints(MinkowskiSum.MinkowskiSumPoint a, MinkowskiSum.MinkowskiSumPoint b, Separation separation) {
        Vector2 p1 = new Vector2();
        Vector2 p2 = new Vector2();

        // find lambda1 and lambda2
//        Vector2 l = a.point.to(b.point);
        Vector2 l = b.point.cpy().sub(a.point);
        // check if a and b are the same point
        if (l.isZero()) {
            // then the closest points are a or b support points
            p1.set(a.supportPoint1);
            p2.set(a.supportPoint2);
        } else {
            // otherwise compute lambda1 and lambda2
            float ll = l.dot(l);
            float l2 = -l.dot(a.point) / ll;

            // check if either lambda1 or lambda2 is less than zero
            if (l2 > 1) {
                // if lambda1 is less than zero then that means that
                // the support points of the Minkowski point B are
                // the closest points
                p1.set(b.supportPoint1);
                p2.set(b.supportPoint2);
            } else if (l2 < 0) {
                // if lambda2 is less than zero then that means that
                // the support points of the Minkowski point A are
                // the closest points
                p1.set(a.supportPoint1);
                p2.set(a.supportPoint2);
            } else {
                // compute the closest points using lambda1 and lambda2
                // this is the expanded version of
                // p1 = a.p1.multiply(1 - l2).add(b.p1.multiply(l2));
                // p2 = a.p2.multiply(1 - l2).add(b.p2.multiply(l2));
                p1.x = a.supportPoint1.x + l2 * (b.supportPoint1.x - a.supportPoint1.x);
                p1.y = a.supportPoint1.y + l2 * (b.supportPoint1.y - a.supportPoint1.y);
                p2.x = a.supportPoint2.x + l2 * (b.supportPoint2.x - a.supportPoint2.x);
                p2.y = a.supportPoint2.y + l2 * (b.supportPoint2.y - a.supportPoint2.y);
            }
        }
        // set the new points in the separation object
        separation.setPoint1(p1);
        separation.setPoint2(p2);
    }
}