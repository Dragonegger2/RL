package com.sad.function.collision.overlay.collision.narrowphase;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.collision.VUtils;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.Circle;
import com.sad.function.collision.overlay.shape.Convex;
import org.dyn4j.Epsilon;

import java.util.ArrayList;
import java.util.List;

public class GJK implements NarrowPhaseDetector {
    public static final float DEFAULT_MAX_ITERATIONS = 30;
    protected EPA epa = new EPA();

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
        if(c1 instanceof Circle && c2 instanceof Circle) {
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
}