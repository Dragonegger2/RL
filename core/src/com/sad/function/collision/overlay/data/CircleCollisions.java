package com.sad.function.collision.overlay.data;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.shape.Circle;
import com.sad.function.collision.overlay.data.Transform;

public class CircleCollisions {

    public static final boolean distance(Circle c1, Transform t1, Circle c2, Transform t2, Penetration penetration) {
        penetration = penetration == null ? new Penetration()  : penetration.clear();
        Vector2 ce1 = t1.getTransformed(c1.getCenter());
        Vector2 ce2 = t2.getTransformed(c2.getCenter());

        float r1 = c1.getRadius();
        float r2 = c2.getRadius();

        Vector2 v = ce1.cpy().sub(ce2);

        float radii = r1 + r2;

        float magnitude = v.len2();

        if(magnitude >= radii * radii) {

            return true;
        }

        return false;
    }
}
