package com.sad.function.collision.overlay.collision.continuous;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.TimeOfImpact;
import com.sad.function.collision.overlay.shape.Convex;
import org.dyn4j.Epsilon;
import org.dyn4j.geometry.Transform;

/**
 * Conservative Advancement
 *
 * See: https://github.com/dyn4j/dyn4j/blob/master/src/main/java/org/dyn4j/collision/continuous/ConservativeAdvancement.java
 */
public class CA {
    public static final float DEFAULT_DISTANCE_EPSILON = (float)Math.cbrt(Epsilon.E);

    public static final int DEFAULT_MAX_ITERATIONS = 30;

    public boolean getTimeOfImpact(Convex convex1, Transform transform1, Vector2 dp1, float dpa,
                                   Convex convex2, Transform transform2, Vector2 dp2, double da2,
                                   double t1, double t2, TimeOfImpact toi) {
        int iterations = 0;

        Transform lerpTx1 = new Transform();
        Transform lerpTx2 = new Transform();

        return false;
    }
}
