package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.shape.Circle;
import com.sad.function.collision.overlay.shape.Projection;
import com.sad.function.collision.overlay.shape.Rectangle;
import com.sad.function.collision.overlay.shape.Shape;

import java.lang.reflect.Array;

import static java.lang.Math.abs;

public class Collision {

    private static Penetration testCollision(Circle a,  Circle b) {
        Penetration p = new Penetration(null,0, null, null);

        //TODO: two circles collide if the distance between their origins is less than the sum of their radii.
        return p;
    }

    private static Penetration testCollision(Rectangle r1, Rectangle r2) {
        //TODO: Simplify the two axis cases for rectangles down to two; You do not need to calculate against parallel axes.
        return null;
    }
    public static Penetration testCollision(Shape a, Shape b) {
        Vector2[] axes1 = a.getAxes();
        Vector2[] axes2 = b.getAxes();

        Vector2 smallest = new Vector2();
        float overlap = Float.POSITIVE_INFINITY;//Really large value.

        for (int i = 0; i < axes1.length; i++) {
            Vector2 axis = axes1[i];

            Projection p1 = project(a, axis);
            Projection p2 = project(b, axis);

            if (!p1.overlaps(p2)) {
                return null;
            } else {
                float o = p1.getOverlap(p2);

                if (p1.contains(p2) || p2.contains(p1)) {
                    float min = abs(p1.getMin() - p2.getMin());
                    float max = abs(p1.getMax() - p2.getMin());

                    if (max > min) {
                        axis.scl(-1);
                        o += min;
                    } else {
                        o += max;
                    }
                }

                if (o < overlap) {
                    overlap = o;
                    smallest = axis;
                }
            }
        }
        for (int i = 0; i < axes2.length; i++) {
            Vector2 axis = axes2[i];

            Projection p1 = project(a, axis);
            Projection p2 = project(b, axis);

            if (!p1.overlaps(p2)) {
                return null;
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
                    smallest = axis;
                }
            }
        }

        //If we make it here, there has been a collision.
        return new Penetration(smallest, overlap, a, b);
    }

    private static Projection project(Shape a, Vector2 axis) {
        // Project the shapes along the axis
        float min = axis.dot(a.getVertex(0, axis)); // Get the first min
        float max = min;
        for (int i = 1; i < a.getNumberOfVertices(); i++) {
            float p = axis.dot(a.getVertex(i, axis)); // Get the dot product between the axis and the node
            if (p < min) {
                min = p;
            } else if (p > max) {
                max = p;
            }
        }
        return new Projection(min, max);
    }

    public static Vector2[] concatenate(Vector2[] a, Vector2[] b) {
        // Concatenate the two arrays of nodes
        int aLen = a.length;
        int bLen = b.length;

        Vector2[] c = (Vector2[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}
