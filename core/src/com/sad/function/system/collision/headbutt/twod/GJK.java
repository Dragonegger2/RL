package com.sad.function.system.collision.headbutt.twod;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class GJK {
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

    public boolean intersects(Shape a, Shape b) {
        Simplex simplex = new Simplex();
        Vector2 direction = new Vector2(1, 0);
        simplex.add(support(a,b, direction));

//        direction.scl(-1);
        direction.set(simplex.getLast().cpy().scl(-1));

        while(true) {
            simplex.add(support(a,b, direction));
            if(simplex.getLast().dot(direction) < 0) {
                return false;
            } else {
                if(containsOrigin(simplex, direction)) {
                    return true;
                }
            }
        }
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
                } else{
                    // otherwise we know its in R5 so we can return true
                    return true;
                }
            }
        } else {        //TODO CORRECT SO FAR. this is the line segment case. NOT
            // then its the line segment case
            Vector2 b = simplex.getB();
            // compute AB
            Vector2 ab = b.cpy().sub(a); //ab = b - a

            if(isSameDirection(ab, ao)) {

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

    class Simplex  {
        ArrayList<Vector2> simplex;

        public Simplex() {
            simplex = new ArrayList<>();
        }

        public Vector2 getA() {
            return simplex.get(simplex.size() - 1);
        }

        public Vector2 getB() {
            return simplex.get(simplex.size() - 2);
        }

        public Vector2 getC() {
            return simplex.get(simplex.size() - 3);
        }

        public void remove(Vector2 removeTarget){
            simplex.remove(removeTarget);
        }

        public void add(Vector2 v) {
            simplex.add(v);
        }

        public Vector2 getLast() { return simplex.get(simplex.size() - 1); }

        public int size() { return simplex.size();}
    }
}