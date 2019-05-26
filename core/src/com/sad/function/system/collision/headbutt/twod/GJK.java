package com.sad.function.system.collision.headbutt.twod;

import com.badlogic.gdx.math.Vector2;

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

        return a.support(direction).sub(b.support(newDirection));
    }

    public boolean intersects(Shape a, Shape b) {
        Vector2 direction = new Vector2(1, 0);
        List<Vector2> simplex = new ArrayList<>();
        simplex.add(support(a, b, direction));                      //Add the initial point to the array.

        direction = simplex.get(0).cpy().scl(-1);

        int accumulator = 0;
        while (accumulator < maxIterations) {
            //I think the support function should invert the direction for me?
            simplex.add(support(a, b, direction)); //Use the inverted direction to check the otherway.
            if (!isSameDirection(direction, simplex.get(0))) {
                return false;
            }

            if (processSimplex(simplex, direction)) {
                return true;
            }

            accumulator++;
        }
        return false;
    }

    boolean containsOrigin(List<Vector2> simplex, Vector2 direction) {
        Vector2 first = simplex.get(2).cpy();
        Vector2 firstFromOrigin = first.cpy().scl(-1);
        Vector2 second,
                third,
                firstSecond,
                firstThird;

        //We've got our 3 vertices.
        if (simplex.size() == 3) {
            second = simplex.get(1).cpy();
            third = simplex.get(0).cpy();
            firstSecond = second.cpy().sub(first);
            firstThird = third.cpy().sub(first);

            direction = new Vector2(firstSecond.y, -1 * firstSecond.x);

            if (direction.dot(third) > 0) {
                direction = direction.scl(-1);
            }

            if (direction.dot(firstFromOrigin) > 0) {
                simplex.remove(0);
                return false;
            }

            direction = new Vector2(firstThird.y, -1 * firstThird.x);

            if (direction.dot(firstFromOrigin) > 0) {
                simplex.remove(1);
                return false;
            }

            return true;
        } else { //Line segment
            second = simplex.get(0).cpy();
            firstSecond = second.cpy().sub(first);

            direction = new Vector2(firstSecond.y, -1 * firstSecond.x);

            if (direction.dot(firstFromOrigin) < 0) {
                direction = direction.scl(-1);
            }

        }

        return false;
    }


    boolean process(List<Vector2> simplex, Vector2 direction) {
        if(simplex.size() == 2 ) {              //Line segment.
            //     ->
            // if  AB
            //      [1] [ A , B ]   AB * AC * AB


        }
        return false;
    }

    //a is the point that was most recently added to the simplex.
    //Updates the simplex and the direction. This is wehre the magic happens!
    //All of my assumptions are wrong.

    boolean processSimplex(List<Vector2> simplex, Vector2 direction) {
        if (simplex.size() == 3) {//1 -simple

            Vector2 AB = simplex.get(1).cpy().sub(simplex.get(0));
            Vector2 AC = simplex.get(2).cpy().sub(simplex.get(0));
            Vector2 A0 = simplex.get(0).cpy().scl(-1);                  //Origin - A = -A

            Vector2 ACB = perpendicular(AB);
            ACB = ACB.scl(ACB.dot(AC.cpy().scl(-1)));

            if (isSameDirection(ACB, A0)) {
                if (isSameDirection(AB, A0)) { //REGION 4
                    direction.set(A0);

                    simplex.remove(1);
                    simplex.remove(2);
                    return false;
                } else {            //REGION 6
                    direction.set(A0);
                    //Remove the b simplex. IE 1.
                    simplex.remove(1);
                    return false;
                }
            }
            return true;
        } else { // Line Segment.
            //We are getting somewhere if this proves true, since this means we are still chasing down the direction of the origin.
            if (isSameDirection(simplex.get(0).cpy().scl(-1), simplex.get(1).cpy().sub(simplex.get(0)))) {
                direction = perpendicular(simplex.get(1).cpy().sub(simplex.get(0)));
                direction.scl(-simplex.get(0).cpy().dot(direction));
            } else {
                //TODO This is where my bug is.
                /*
                This is supposed to always use A as the most recent value.
                 */
                //B is not the valid case, remove it.
                simplex.remove(1);
                direction = simplex.get(0).scl(-1);
                //Remove b simple ie 1.
            }
        }

        return false;
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

    private boolean  isSameDirection(Vector2 v1, Vector2 v2) {
        return v1.dot(v2) > 0;
    }

//    /**
//     * This class represents a simplex in 2D, as such it only has 3 points.
//     * A -> the most recently added point.
//     * B -> the second most recently added point.
//     * C -> The Last added point.
//     */
//    private class Simplex {
//        private
//        private Vector2 a;
//        private Vector2 b;
//        private Vector2 c;
//
//        public Vector2 A() {
//            return a;
//        }
//        public Vector2 B() {
//            return b;
//        }
//
//        public Vector2 C() {
//            return c;
//        }
//    }
}