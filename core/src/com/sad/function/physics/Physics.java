package com.sad.function.physics;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.cd.shapes.Circle;
import com.sad.function.system.cd.shapes.Polygon;
import com.sad.function.system.cd.shapes.Shape;

import java.util.Iterator;
import java.util.List;

public class Physics {
    public static boolean rayCast(Ray ray, Shape shape, RayHit hit, float distance) {
        if (shape instanceof Circle) {
            return handleRayCircle(ray, (Circle) shape, hit);
        }

        if (shape instanceof Polygon) {
            return handleRayPolygon(ray, (Polygon) shape, hit, distance);
        }

        return false;
    }

    public static boolean rayCast(Ray ray, List<Shape> bodies, RayHit hit, float distance) {
        float min = Float.MIN_VALUE;
        boolean found = false;
        boolean currentOperation;

        for (Shape body : bodies) {
            RayHit temp = new RayHit();
            currentOperation = rayCast(ray, body, temp, distance);
            if (currentOperation) {
                if (temp.getCollisionPoint().y > min) {
                    found = true;
                    min = temp.getCollisionPoint().y;
                    hit.setpNormal(temp.getpNormal());
                    hit.setpDepth(temp.getpDepth());
                    hit.setCollisionPoint(temp.getCollisionPoint());
                }
            }

        }
        return found;
    }

    private static boolean handleRayCircle(Ray ray, Circle circle, RayHit hit) {
        //TODO IMPLEMENT THIS CASE.
        return false;
    }

    private static boolean handleRayPolygon(Ray ray, Polygon polygon, RayHit hit, float distance) {
        Vector2[] vertices = polygon.getVertices();

        float x1 = ray.getOrigin().x;
        float y1 = ray.getOrigin().y;

        Vector2 end = ray.getOrigin().cpy().add(ray.getDirection().scl(distance));
//        float x2 = ray.getOrigin().adgetEnd().x;
//        float y2 = ray.getEnd().y;
        float x2 = end.x;
        float y2 = end.y;
        int next;
        for (int current = 0; current < vertices.length; current++) {
            next = current + 1;
            if (next == vertices.length)
                next = 0;

            float x3 = vertices[current].x;
            float y3 = vertices[current].y;
            float x4 = vertices[next].x;
            float y4 = vertices[next].y;

            if (lineLine(x1, y1, x2, y2, x3, y3, x4, y4)) {
                //TODO: Calculate intersection point.
                //TODO: Calculate penetration depth.
                //TODO: Calculate penetration normal

                hit.setCollisionPoint(calculateIntersectionPoint(x1, y1, x2, y2, x3, y3, x4, y4));
                Vector2 pNormal = hit.getCollisionPoint().cpy().sub(end);

                hit.setpDepth(pNormal.len());
                hit.setpNormal(pNormal.nor());
                return true;
            }
        }

        return false;
    }

    /**
     * Calculate if two line segments intersect.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return if the provided points of two line segments intersect.
     */
    public static boolean lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

        // calculate the setDirection of the lines
        float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

        // if uA and uB are between 0-1, lines are colliding
        return uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1;

    }

    /**
     * Calculate the intersection point of two line segements. NOTE, these coordinates should only be trusted after checking for collision.
     * This ensures that you do not waste time calculating intersection points of lines that will intersect far beyond their segments length.
     *
     * @param x1 x value, point 1 line segment 1
     * @param y1 y vlaue, point 1 line segment 1
     * @param x2 x value, point 2 line segment 1
     * @param y2 y value, point 2 line segment 1
     * @param x3 x value, point 3 line segment 2
     * @param y3 y value, point 3 line segment 2
     * @param x4 x value, point 4 line segment 2
     * @param y4 x value, point 4 line segment 2
     * @return the point of intersection of two lines.
     */
    public static Vector2 calculateIntersectionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float divisor = ((x2 - x1) * (y4 - y3) - (x4 - x3) * (y2 - y1));

        float x = ((x2 * y1 - x1 * y2) * (x4 - x3) - (x4 * y3 - x3 * y4) * (x2 - x1))
                / divisor;

        float y = ((x2 * y1 - x1 * y2) * (y4 - y3) - (x4 * y3 - x3 * y4) * (y2 - y1))
                / divisor;

        return new Vector2(x, y);
    }
}
