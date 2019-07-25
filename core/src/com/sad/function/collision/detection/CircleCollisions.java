package com.sad.function.collision.detection;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.data.Penetration;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Circle;

/**
 * Contains a static method for detecting the {@link Penetration} information about two {@link Circle}.
 */
public class CircleCollisions {
    public static boolean distance(Circle c1, Transform t1, Circle c2, Transform t2, Penetration penetration) {
        penetration = penetration == null ? new Penetration()  : penetration.clear();
        Vector2 ce1 = t1.getTransformed(c1.getCenter());
        Vector2 ce2 = t2.getTransformed(c2.getCenter());

        float r1 = c1.getRadius();
        float r2 = c2.getRadius();

        Vector2 v = ce1.sub(ce2);

        float radii = r1 + r2;

        float magnitude = v.len2();

        return magnitude >= radii * radii;
    }
}
