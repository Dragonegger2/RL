package com.sad.function.collision.detection.narrowphase;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.Epsilon;
import com.sad.function.collision.detection.DistanceDetector;
import com.sad.function.collision.data.Penetration;
import com.sad.function.collision.data.Separation;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.data.VUtils;
import com.sad.function.collision.geometry.Ray;
import com.sad.function.collision.geometry.Raycast;
import com.sad.function.collision.geometry.Raycaster;
import com.sad.function.collision.shape.Circle;
import com.sad.function.collision.shape.Convex;


import java.util.ArrayList;
import java.util.List;

public class GJK implements NarrowPhaseDetector, DistanceDetector, Raycaster {
    public static final int DEFAULT_MAX_ITERATIONS = 30;
    public static final float DEFAULT_DETECT_EPSILON = (float) Epsilon.E;
    public static final float DEFAULT_DISTANCE_EPSILON = (float) Math.sqrt(Epsilon.E);

    private static final Vector2 ORIGIN = new Vector2(0, 0);

    protected int maxDistanceIterations = GJK.DEFAULT_MAX_ITERATIONS;
    protected float detectEpsilon = GJK.DEFAULT_DETECT_EPSILON;
    protected float distanceEpsilon = GJK.DEFAULT_DISTANCE_EPSILON;

    protected EPA epa = new EPA();
    private float raycastEpsilon = GJK.DEFAULT_DISTANCE_EPSILON;

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

        return point1.sub(point2);
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

        MinkowskiSum ms = new MinkowskiSum(c1, t1, c2, t2);

        //Find the initial search direction and populate the simplex with it; prevents the 0th case of the GJK alg.
        Vector2 d = getInitialDirection(c1, t1, c2, t2);

//        if (d.isZero()) d.set(1, 0);

        if(detect(ms, simplex, d)) {
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

    protected boolean detect(MinkowskiSum ms, List<Vector2> simplex, Vector2 d) {
        // check for a zero direction vector
        if (d.isZero()) d.set(1f, 0);
        // add the first point
        simplex.add(ms.getSupportPoint(d));
        // is the support point past the origin along d?
        if (simplex.get(0).dot(d) <= 0.0) {
            return false;
        }
        // negate the search direction
        d.scl(-1);
        // start the loop
        for (int i = 0; i < DEFAULT_MAX_ITERATIONS; i++) {
            // always add another point to the simplex at the beginning of the loop
            Vector2 supportPoint = ms.getSupportPoint(d);
            simplex.add(supportPoint);

            // make sure that the last point we added was past the origin
            if (supportPoint.dot(d) <= this.detectEpsilon) {
                // a is not past the origin so therefore the shapes do not intersect
                // here we treat the origin on the line as no intersection
                // immediately return with null indicating no penetration
                return false;
            } else {
                // if it is past the origin, then test whether the simplex contains the origin
                if (this.checkSimplex(simplex, d)) {
                    // if the simplex contains the origin then we know that there is an intersection.
                    // if we broke out of the loop then we know there was an intersection
                    return true;
                }
                // if the simplex does not contain the origin then we need to loop using the new
                // search direction and simplex
            }
        }
        return false;
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

            d.set(VUtils.tripleProduct(ab, ao, ab));

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

        Vector2 d = c2.cpy().sub(c1);  //c1.to(c2)

        if (d.isZero()) return false;

        a = ms.getSupportPoints(d);

        d.scl(-1);

        b = ms.getSupportPoints(d);

        d = Segment.getPointOnSegmentClosestToPoint(ORIGIN, b.point, a.point);
        for (int i = 0; i < DEFAULT_MAX_ITERATIONS; i++) {
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

            if (p1Magnitude <= Epsilon.E) {
                d.nor();
                separation.setDistance(p1.len());
                p1.nor();
                separation.setNormal(d);
                this.findClosestPoints(a, c, separation);
                return true;
            } else if (p2Magnitude <= Epsilon.E) {
                d.nor();
                separation.setDistance(p2.len());
                p2.nor();
                separation.setNormal(d);
                this.findClosestPoints(c, b, separation);
                return true;
            }

            if (p1Magnitude < p2Magnitude) {
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

    @Override
    public boolean raycast(Ray ray, float maxLength, Convex convex, Transform transform, Raycast raycast) {
        //TODO Circles
        //TODO SEGMENTS

        float lambda = 0;

        boolean lengthCheck = maxLength > 0;

        Vector2 a = null;
        Vector2 b = null;

        Vector2 start = ray.getStart();

        Vector2 x = start;

        Vector2 r = ray.getDirectionVector();
        Vector2 n = new Vector2();

        //is the starting point already contained within the convex shape.
        if (convex.contains(start, transform)) {
            return false;
        }

        Vector2 c = transform.getTransformed(convex.getCenter());
        Vector2 d = x.cpy().sub(c); //c.to(x);

        float distanceSquared = Float.MAX_VALUE;
        int iterations = 0;

        while (distanceSquared > raycastEpsilon) {
            Vector2 p = convex.getFarthestPoint(d, transform);

            Vector2 w = x.cpy().sub(p);

            float dDotW = d.dot(w);

            if (dDotW > 0) {
                float dDotR = d.dot(r);

                if (dDotR >= 0) {
                    return false;
                } else {
                    lambda = lambda - dDotW / dDotR;

                    if (lengthCheck && lambda > maxLength) {
                        return false;
                    }

                    //x = r.product(lambda).add(start);
                    x = r.cpy().scl(lambda).add(start);
                    n.set(d);
                }
            }

            if (a != null) {
                if (b != null) {
                    Vector2 p1 = Segment.getPointOnSegmentClosestToPoint(x, a, p);
                    Vector2 p2 = Segment.getPointOnSegmentClosestToPoint(x, p, b);

                    //Distance squared from points.
                    if (VUtils.distanceSquared(p1, x) < VUtils.distanceSquared(p2, x)) {
                        b.set(p);
                        distanceSquared = VUtils.distanceSquared(p1, x);
                    } else {
                        a.set(p);
                        distanceSquared = VUtils.distanceSquared(p2, x);
                    }

                    Vector2 ab = b.cpy().sub(a);
                    Vector2 ax = x.cpy().sub(a);

                    d = VUtils.tripleProduct(ab, ax, ab);
                } else {
                    b = p;

                    Vector2 ab = b.cpy().sub(a);
                    Vector2 ax = x.cpy().sub(a);

                    d = VUtils.tripleProduct(ab, ax, ab);
                }
            } else {
                a = p;
                d.scl(-1);
            }

            //We were unable to find a colliding body fixture in the allotted number of iterations.
            if (iterations == maxDistanceIterations) {
                return false;
            }

            iterations++;
        }

        raycast.setPoint(x);
        raycast.setNormal(n);
        raycast.getNormal().nor(); //Set the normal, and then make sure it is normalized.
        raycast.setDistance(lambda);

        return true;
    }
}