package com.sad.function.system.collision.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.collision.shapes.Shape;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.List;

@SuppressWarnings("ALL")
public class CollisionDetectionAlgorithms {

    /**
     * The maximum number of simplex evolution iterations before we accept the
     * given simplex. For non-curvy shapes, this can be low. Curvy shapes potentially
     * require higher numbers, but this can introduce significant slow-downs at
     * the gain of not much accuracy.
     */
    public int maxIterations = 20;

    public static Vector2 tripleProduct(Vector2 a, Vector2 b, Vector2 c) {
        Vector2 r = new Vector2();

        float dot = a.x * b.y - b.x * a.y;
        r.x = -c.y * dot;
        r.y = c.x * dot;

        return r;
    }

    /**
     * Calculates a single point in the Minkowski Difference in a given direction
     */
    private Vector2 minkowskiPoint(Shape a, Shape b, Vector2 direction) {
        Vector2 first = a.support(direction);
        Vector2 newDirection = new Vector2(direction).scl(-1);
        Vector2 second = b.support(newDirection);

        return first.sub(second);
    }

    public boolean gjk(Shape a, Shape b) {
        Simplex simplex = new Simplex();
        Vector2 direction = new Vector2(1, 0);
        simplex.add(minkowskiPoint(a, b, direction));

        //TODO Instead of being arbitrary, i could look up the collision if I stored them and use the witness points to create a suitable direction angle to save time. I think...?
        direction.set(simplex.getLast().cpy().scl(-1));

        int accumulator = 0;
        while (accumulator < maxIterations) {            //Prevent infinite problems.
            simplex.add(minkowskiPoint(a, b, direction));
            if (simplex.getLast().dot(direction) < 0) {
                return false;
            } else {
                if (containsOrigin(simplex, direction)) {
                    return true;
                }
            }
            accumulator++;
        }

        //No intersections could be found in the alloted iterations.
        return false;
    }

    public boolean containsOrigin(Simplex simplex, Vector2 d) {
        // get the last point added to the simplex
        Vector2 a = simplex.getLast();

        // compute AO (same thing as -A)
        Vector2 ao = a.cpy().scl(-1);

        if (simplex.size() == 3) {
            // then its the triangle case
            // get b and c
            Vector2 b = simplex.getB();
            Vector2 c = simplex.getC();

            // compute the edges
            Vector2 ab = b.cpy().sub(a);
            Vector2 ac = c.cpy().sub(a);

            // compute the normals
            Vector2 abPerp = tripleProduct(ac, ab, ab);
            Vector2 acPerp = tripleProduct(ab, ac, ac);
            // is the origin in R4
            if (isSameDirection(abPerp, ao)) {
                // remove point c
                simplex.remove(c);
                // set the new direction to abPerp
                d.set(abPerp);
            } else {
                // is the origin in R3
                if (acPerp.dot(ao) > 0) {
                    // remove point b
                    simplex.remove(b);
                    // set the new direction to acPerp
                    d.set(acPerp);
                } else {
                    // otherwise we know its in R5 so we can return true
                    return true;
                }
            }
        } else {
            // then its the line segment case
            Vector2 b = simplex.getB();
            // compute AB
            Vector2 ab = b.cpy().sub(a); //ab = b - a

            if (isSameDirection(ab, ao)) {

                Vector2 abPerp = tripleProduct(ab, ao, ab); // get the perp to AB in the direction of the origin
                d.set(abPerp); // set the direction to abPerp
            } else {
                simplex.remove(b);
                d.set(ao);
            }

        }
        return false;
    }

    //a is the point that was most recently added to the simplex.
    //Updates the simplex and the direction. This is wehre the magic happens!
    // a = last index
    boolean processSimplex(List<Vector2> simplex, Vector2 direction) {
        if (simplex.size() == 2) {
            // Line Segment.
            Vector2 a = simplex.get(simplex.size() - 1);
            Vector2 b = simplex.get(simplex.size() - 2);

            //isSameDirection(-a, b.sub(a)_
            if (isSameDirection(simplex.get(1).cpy().scl(-1), simplex.get(0).cpy().sub(simplex.get(1)))) {
                //perpendicular(b-a)
                direction = perpendicular(simplex.get(0).cpy().sub(simplex.get(1)));
                direction.scl(-simplex.get(1).cpy().dot(direction));
            } else {
                direction = simplex.get(1).cpy().scl(-1);
                //Remove b simple ie 1.
                simplex.remove(0);
            }

            return true;
        } else {
            int a = 2;
            int b = 1;
            int c = 0;
            Vector2 AB = simplex.get(b).cpy().sub(simplex.get(a));
            Vector2 AC = simplex.get(c).cpy().sub(simplex.get(a));
            Vector2 A0 = simplex.get(a).cpy().scl(-1);

            Vector2 ACB = perpendicular(AB);
            ACB = ACB.scl(ACB.dot(AC.cpy().scl(-1)));
            Vector2 ABC = perpendicular(AC);
            ABC = ABC.scl(ABC.dot(AB.cpy().scl(-1)));

            if (isSameDirection(ACB, A0)) {
                if (isSameDirection(AB, A0)) { //REGION 4
                    direction.set(ACB);
                    simplex.remove(c);

                    return false;
                } else {            //REGION 5
                    direction.set(A0);
                    //Remove the b simplex. IE 1.
                    simplex.remove(0);//Remove c;
                    simplex.remove(0);//Remove a;
                    return false;
                }
            } else if (isSameDirection(ABC, A0)) {
                if (isSameDirection(AC, A0)) { //Region 6
                    direction.set(ABC);
                    simplex.remove(b);
                    return false;
                } else { //Region 5 again
                    direction.set(A0);
                    simplex.remove(0);
                    simplex.remove(0);
                    return false;
                }
            } else {
                return true;
            }
        }
    }

//    public boolean checkSimplex(List<Vector2> s, Vector2 direction) {
//        Vector2 a = s.get(simplex.size() - 1).cpy();
//        Vector2 ao = a.cpy().scl(-1);   //origin - a = -a
//
//        if (s.size() == 3) {
//            Vector2 b = s.get(1).cpy();
//            Vector2 c = s.get(0).cpy();
//
//            Vector2 ab = b.cpy().sub(a); //b - a
//            Vector2 ac = c.cpy().sub(a); //c - a
//
//            Vector2 acPerp = new Vector2();
//            float dot = ab.x * ac.y - ac.x * ab.y;
//            acPerp.x = -ac.y * dot;
//            acPerp.y = ac.x * dot;
//
//            float acLocation = acPerp.dot(ao);
//            if (acLocation >= 0.0) {
//                s.remove(1);
//                direction.set(acPerp);
//            } else {
//                Vector2 abPerp = new Vector2();
//                abPerp.x = ab.y * dot;
//                abPerp.y = -ab.x * dot;
//
//                double abLocation = abPerp.dot(ao);
//
//                if (abLocation < 0.0f) {
//                    return true;
//                } else {
//                    s.remove(0);
//                    direction.set(abPerp);
//                }
//            }
//        } else {
//            Vector2 b = s.get(0);
//            Vector2 ab = b.cpy().sub(a);
//
//            direction.set(tripleProduct(ab, ao, ab));
//
//            if (direction.len2() <= 0.00001f) {
//                direction.set(left(ab));
//            }
//        }
//
//        return false;
//    }

    public Vector2 left(Vector2 a) {
        float temp = a.x;
        a.x = a.y;
        a.y = -temp;

        return a;
    }

    /**
     * Creates a vector that is perpendicular to the provided vector.
     * Rotate by 90 degrees such that you create a new vector of (-v.y, v.x)
     *
     * @param v to calculate the perpendicular
     * @return the perpendicular vector.
     */
    private Vector2 perpendicular(Vector2 v) {
        return new Vector2(-v.y, v.x);
    }

    private boolean isSameDirection(Vector2 v1, Vector2 v2) {
        return v1.dot(v2) > 0;
    }

    public void getPenetration(Simplex simplex, MinkowskiSum minkowskiSum, Vector2 penetration) {
        Edge edge = null;
        Vector2 point = null;
        for(int i = 0; i < this.maxIterations; i++) {
            edge = findClosestEdge(simplex);
            point = minkowskiSum.getSupportPoints(edge.normal);

            float project = point.dot(edge.normal);
            if((project - edge.distance) < Epsilon.E) {
                penetration.set(edge.normal).scl(project);
                return;
            }

//            simplex
        }
    }

    public Edge findClosestEdge(Simplex simplex) {
        Edge closest;
        closest = new Edge();
        closest.distance = Float.MAX_VALUE;

        for (int i = 0; i < simplex.get().size() - 1; i++) {
            int j;

            if (i + 1 == simplex.get().size()) {
                j = 0;
            } else {
                j = i + 1;
            }

            Vector2 a = simplex.get(i).cpy();
            Vector2 b = simplex.get(j).cpy();

            Vector2 e = b.cpy().sub(a);

            Vector2 oa = a.cpy();

            Vector2 n = tripleProduct(e, oa, e);

            n.nor();

            float d = n.dot(a);

            if (d < closest.distance) {
                closest.distance = d;
                closest.normal = n;
                closest.index = j;
            }
        }
        return closest;
    }

//    public Vector2 intersect(Shape a, Shape b) {
//        if (!gjk(a, b)) {
//            return null;
//        }
//
//        float e0 = (simplex.get(1).x - simplex.get(0).x) * (simplex.get(1).y + simplex.get(0).y);
//        float e1 = (simplex.get(2).x - simplex.get(1).x) * (simplex.get(2).y + simplex.get(1).y);
//        float e2 = (simplex.get(0).x - simplex.get(2).x) * (simplex.get(0).y + simplex.get(2).y);
//
//        PolygonWinding winding = e0 + e1 + e2 >= 0 ? PolygonWinding.Clockwise : PolygonWinding.CounterClockwise;
//
//        Vector2 intersection = new Vector2();
//
//        for (int i = 0; i <= maxIterations; i++) {
//            Edge edge = findClosestEdge(simplex);               //Get closest edge
//            Vector2 support = minkowskiPoint(a, b, edge.normal);       //Get minkowskiPoint in the direction of the edge that is closest to the origin
//            float distance = support.dot(edge.normal);
//
//            if (Math.abs(distance - edge.distance) <= 0.000001) {
//                return intersection;
//            } else {
//                simplex.get().add(edge.index, support);
//            }
//
//            intersection = edge.normal.cpy();
//            intersection = intersection.scl(distance);
//        }
//
//        return intersection;
//    }

    private static class Segment {
        public static Vector2 getPointOnSegementClosestToPoint(Vector2 point, Vector2 linePoint1, Vector2 linePoint2) {
            Vector2 p1ToP = point.cpy().sub(linePoint1);
            Vector2 line = linePoint2.cpy().sub(linePoint1);

            float ab2 = line.dot(line);
            float ap_ab = p1ToP.dot(line);

            if (ab2 <= Epsilon.E) return linePoint1.cpy();

            float t = ap_ab / ab2;

            t = MathUtils.clamp(t, 0.0f, 1.0f);

            return line.cpy().mulAdd(linePoint1, t);
        }
    }

    public static final class Epsilon {
        public static final float E = Epsilon.compute();

        private Epsilon() {
        }

        public static final float compute() {
            float e = 0.5f;
            while (1.0f + e > 1.0) {
                e *= 0.5;
            }
            return e;

        }
    }

    private class MinkowskiSum {
        private Shape a;
        private Shape b;

        public MinkowskiSum(Shape a, Shape b) {
            this.a = a;
            this.b = b;
        }

        public Vector2 getSupportPoints(Vector2 direction) {
            Vector2 first = a.support(direction);
            Vector2 second = b.support(direction.scl(-1));
            direction.scl(-1);

            return first.sub(second);
        }
    }

    private class Separation {
        public Vector2 normal;
        public float distance;
        public Vector2 point1;
        public Vector2 point2;
    }
}