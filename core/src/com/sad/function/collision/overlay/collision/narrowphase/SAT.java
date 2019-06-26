package com.sad.function.collision.overlay.collision.narrowphase;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Projection;
import com.sad.function.collision.overlay.shape.Circle;
import com.sad.function.collision.overlay.shape.Convex;
import com.sad.function.collision.overlay.shape.Rectangle;
import com.sad.function.collision.overlay.data.Transform;

import static java.lang.Math.abs;

public class SAT implements NarrowPhaseDetector {

    private static Penetration detect(Circle a, Circle b) {
        //TODO: two circles collide if the distance between their origins is less than the sum of their radii.
        return new Penetration(null,0, null, null);
    }

    private static Penetration detect(Rectangle r1, Rectangle r2) {
        //TODO: Simplify the two axis cases for rectangles down to two; You do not need to calculate against parallel axes.
        return null;
    }

    @Override
    public boolean detect(Convex convex1, Transform transform1, Convex convex2, Transform transform2) {

        Vector2[] foci1 = convex1.getFoci(transform1);
        Vector2[] foci2 = convex2.getFoci(transform2);

        Vector2[] axes1 = convex1.getAxes(foci2, transform1);
        Vector2[] axes2 = convex2.getAxes(foci1, transform2);

        Vector2 n = new Vector2();

        int size = axes1.length;
        for (int i = 0; i < size; i++) {
            Vector2 axis = axes1[i];

            Projection p1 = convex1.project(axis, transform1);
            Projection p2 = convex2.project(axis, transform2);

            if (!p1.overlaps(p2)) {
                return false;
            }
        }
        for (int i = 0; i < axes2.length; i++) {
            Vector2 axis = axes2[i];

            Projection p1 = convex1.project(axis, transform1);
            Projection p2 = convex2.project(axis, transform2);

            if (!p1.overlaps(p2)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean detect(Convex convex1, Transform transform1, Convex convex2, Transform transform2, Penetration penetration) {
        penetration = (penetration == null) ? new Penetration() : penetration.clear();

        Vector2[] foci1 = convex1.getFoci(transform1);
        Vector2[] foci2 = convex2.getFoci(transform2);

        Vector2[] axes1 = convex1.getAxes(foci2, transform1);
        Vector2[] axes2 = convex2.getAxes(foci1, transform2);

        Vector2 n = new Vector2();

        float overlap = Float.POSITIVE_INFINITY;//Really large value.

        int size = axes1.length;
        for (int i = 0; i < size; i++) {
            Vector2 axis = axes1[i];

            Projection p1 = convex1.project(axis, transform1);
            Projection p2 = convex2.project(axis, transform2);

            if (!p1.overlaps(p2)) {
                return false;
            } else {
                float o = p1.getOverlap(p2);

                if (p1.contains(p2) || p2.contains(p1)) {
                    float min = abs(p1.getMin() - p2.getMin());
                    float max = abs(p1.getMax() - p2.getMax());

                    if (max > min) {
                        axis.scl(-1);
                        o += min;
                    } else {
                        o += max;
                    }
                }

                if (o < overlap) {
                    overlap = o;
                    n = axis;
                }
            }
        }
        for (int i = 0; i < axes2.length; i++) {
            Vector2 axis = axes2[i];

            Projection p1 = convex1.project(axis, transform1);
            Projection p2 = convex2.project(axis, transform2);

            if (!p1.overlaps(p2)) {
                return false;
            } else {
                float o = p1.getOverlap(p2);
                if (p1.contains(p2) || p2.contains(p1)) {
                    float max = abs(p1.getMax() - p2.getMax());
                    float min = abs(p1.getMin() - p2.getMin());

                    if (max > min) {
                        axis.scl(-1);
                        o += min;
                    } else {
                        o += max;
                    }
                }
                if (o < overlap) {
                    overlap = o;
                    n = axis;
                }
            }
        }

        Vector2 c1 = transform1.getTransformed(convex1.getCenter());
        Vector2 c2 = transform2.getTransformed(convex2.getCenter());
        Vector2 cToC = c2.cpy().sub(c1);

        if(cToC.dot(n) < 0) {
            n.scl(-1);
        }

        penetration.distance = overlap;
        penetration.normal = n;
        penetration.a = convex1;
        penetration.b = convex2;

        return true;
    }
}
