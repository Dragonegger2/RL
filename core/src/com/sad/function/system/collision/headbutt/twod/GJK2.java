package com.sad.function.system.collision.headbutt.twod;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class GJK2 {
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

    //a is the point that was most recently added to the simplex.
    //Updates the simplex and the direction. This is wehre the magic happens!
    // a = last index
    boolean processSimplex(List<Vector2> simplex, Vector2 direction) {
        if (simplex.size() == 2) {
            Vector2 a = simplex.get(simplex.size() - 1);
            Vector2 b = simplex.get(simplex.size() - 2);

//            Vector2 AB = a.cpy().sub(b); TODO Had this shit flipped.
            Vector2 AB = b.cpy().sub(a);
            Vector2 A0 = a.cpy().scl(-1);

            if(AB.dot(A0) > 0) {
//                direction = AB.cpy().crs(A0).
            }

            // Line Segment.

            //isSameDirection(-a, b.sub(a)_
            if (isSameDirection(simplex.get(1).cpy().scl(-1), simplex.get(0).cpy().sub(simplex.get(1)))) {
                //perpendicular(b-a)
                direction = perpendicular(simplex.get(0).cpy().sub(simplex.get(1)));
                direction.scl(-simplex.get(1).cpy().dot(direction));
            } else {
                direction = simplex.get(1).scl(-1);
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
            } else if(isSameDirection(ABC, A0)) {
                if(isSameDirection(AC, A0)) { //Region 6
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
}