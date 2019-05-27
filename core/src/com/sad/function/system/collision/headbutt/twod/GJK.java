package com.sad.function.system.collision.headbutt.twod;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

@SuppressWarnings("ALL")
public class GJK {
    private static Simplex simplex;

    /**
     * The maximum number of simplex evolution iterations before we accept the
     * given simplex. For non-curvy shapes, this can be low. Curvy shapes potentially
     * require higher numbers, but this can introduce significant slow-downs at
     * the gain of not much accuracy.
     */
    public int maxIterations = 20;

    /**
     * Calculates a single point in the Minkowski Difference.
     */
    private Vector2 support(Shape a, Shape b, Vector2 direction) {
        Vector2 first = a.support(direction);
        Vector2 newDirection = new Vector2(direction).scl(-1);
        Vector2 second = b.support(newDirection);

        return first.sub(second);
    }

    public boolean gjk(Shape a, Shape b) {
        simplex = new Simplex();
        Vector2 direction = new Vector2(1, 0);
        simplex.add(support(a, b, direction));

        direction.set(simplex.getLast().cpy().scl(-1));

        int accumulator = 0;
        while (accumulator < maxIterations) {            //Prevent infinite problems.
            simplex.add(support(a, b, direction));
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


    public Vector2 tripleProduct(Vector2 a, Vector2 b, Vector2 c) {
        Vector3 A = new Vector3(a.x, a.y, 0);
        Vector3 B = new Vector3(b.x, b.y, 0);
        Vector3 C = new Vector3(c.x, c.y, 0);

        Vector3 calc = new Vector3(A).crs(B);
        calc.crs(C);

        return new Vector2(calc.x, calc.y);
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

    private Edge findClosestEdge(Simplex s) {
        Edge closest = new Edge();
        closest.distance = Float.MAX_VALUE;

        for (int i = 0; i < s.get().size() - 1; i++) {
            int j;

            if (i + 1 == simplex.get().size()) {
                j = 0;
            } else {
                j = i + 1;
            }

            Vector2 a = s.get(i).cpy();
            Vector2 b = s.get(j).cpy();

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

    public Vector2 intersect(Shape a, Shape b) {
        if (!gjk(a, b)) {
            return null;
        }

        float e0 = (simplex.get(1).x - simplex.get(0).x) * (simplex.get(1).y + simplex.get(0).y);
        float e1 = (simplex.get(2).x - simplex.get(1).x) * (simplex.get(2).y + simplex.get(1).y);
        float e2 = (simplex.get(0).x - simplex.get(2).x) * (simplex.get(0).y + simplex.get(2).y);

        PolygonWinding winding = e0 + e1 + e2 >= 0 ? PolygonWinding.Clockwise : PolygonWinding.CounterClockwise;

        Vector2 intersection = new Vector2();

        for (int i = 0; i <= 32; i++) {
            Edge edge = findClosestEdge(PolygonWinding.CounterClockwise);
            Vector2 support = support(a, b, edge.normal);
            float distance = support.dot(edge.normal);

            intersection = edge.normal.cpy();
            intersection = intersection.scl(distance);

            if (Math.abs(distance - edge.distance) <= 0.000001) {
                return intersection;
            } else {
                simplex.get().add(edge.index, support);
            }
        }

        return intersection;
    }

    private Edge findClosestEdge(PolygonWinding winding) {
        double closestDistance = Double.MAX_VALUE;
        Vector2 closestNormal = new Vector2();
        int closestIndex = 0;
        Vector2 line = new Vector2();
        for (int i = 0; i < simplex.get().size(); i++) {
            int j = i + 1;
            if (j >= simplex.size() - 1) j = 0;

            line = simplex.get(j);
            line.sub(simplex.get(i));

            Vector2 norm;
            if (winding == PolygonWinding.Clockwise) {
                norm = new Vector2(line.y, -line.x);
            } else {
                norm = new Vector2(-line.y, line.x);
            }

            norm = norm.nor();

            // calculate how far away the edge is from the origin
            double dist = norm.dot(simplex.get(i));
            if (dist < closestDistance) {
                closestDistance = dist;
                closestNormal = norm;
                closestIndex = j;
            }
        }

        return new Edge(closestDistance, closestNormal, closestIndex);
    }

}