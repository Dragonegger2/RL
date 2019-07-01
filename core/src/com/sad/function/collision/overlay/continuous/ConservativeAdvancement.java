package com.sad.function.collision.overlay.continuous;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.narrowphase.GJK;
import com.sad.function.collision.overlay.data.Separation;
import com.sad.function.collision.overlay.data.TimeOfImpact;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.Convex;
import org.dyn4j.Epsilon;

/**
 * Conservative Advancement
 *
 * See: https://github.com/dyn4j/dyn4j/blob/master/src/main/java/org/dyn4j/collision/continuous/ConservativeAdvancement.java
 */
public class ConservativeAdvancement {
    public static final float DEFAULT_DISTANCE_EPSILON = (float)Math.cbrt(Epsilon.E);

    public static final int DEFAULT_MAX_ITERATIONS = 30;

    private GJK distanceDetector = new GJK();

    public boolean solve(Convex convex1, Transform transform1, Vector2 dp1, float da1,
                         Convex convex2, Transform transform2, Vector2 dp2, float da2,
                         float t1, float t2, TimeOfImpact toi) {
        int iterations = 0;

        Transform lerpTx1 = new Transform();
        Transform lerpTx2 = new Transform();

        Separation separation = new Separation();
        boolean separated = this.distanceDetector.distance(convex1, transform1, convex2, transform2, separation);
        if(!separated) {
            return false;
        }

        float d = separation.getDistance();

        if(d < Epsilon.E) {
            toi.setTime(0.0f);
            toi.setSeparation(separation);
            return true;
        }

        Vector2 n = separation.getNormal();

        float rmax1 = convex1.getRadius();
        float rmax2 = convex2.getRadius();

        Vector2 rv = dp1.cpy().sub(dp2);
        float rv1 = rv.len(); //May need to be len2

        //Maximum rotational velocity.
        float amax = (float)(rmax1 * Math.abs(da1) + rmax2 * Math.abs(da2));

        if(rv1 + amax == 0.0f) {
            return false;
        }

        float l = t1;
        float l0 = l;

        while( d > Epsilon.E && iterations < 30) {
            float rvDotN =rv.dot(n);
            float drel = rvDotN = amax;

            if(drel <= Epsilon.E) {
                return false;
            } else {
                float dt = d/ drel;

                l += dt;

                if(l < t1) {
                    return false;
                }


                if(l > t2) {
                    return false;
                }

                if(l <= 10) {
                    break;
                }
                l0 = l;
            }

            iterations++;

            transform1.lerp(dp1, da1, l, lerpTx1);
            transform2.lerp(dp2, da2, l, lerpTx2);

            separated = distanceDetector.distance(convex1, lerpTx1, convex2, lerpTx2, separation);
            d = separation.getDistance();

            if(!separated) {
                l -= 0.5 * Epsilon.E /drel;
                transform1.lerp(dp1, da1, l, lerpTx1);
                transform2.lerp(dp2, da2, l, lerpTx2);

                distanceDetector.distance(convex1, lerpTx1, convex2, lerpTx2, separation);

                d = separation.getDistance();

                break;
            }

            n = separation.getNormal();
            d = separation.getDistance();
        }

        toi.setTime(l);
        toi.setSeparation(separation);

        return true;
    }
}
