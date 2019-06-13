package com.sad.function.collision.differ;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.differ.data.RayCollision;
import com.sad.function.collision.differ.data.RayIntersection;
import com.sad.function.collision.differ.data.ShapeCollision;
import com.sad.function.collision.differ.shapes.Circle;
import com.sad.function.collision.differ.shapes.Polygon;
import com.sad.function.collision.differ.shapes.Ray;

import java.util.List;

/**
 * Java implementation of Collision:
 * https://github.com/snowkit/differ/tree/master/differ
 */
public class SAT2D {
    static ShapeCollision tmp1 = new ShapeCollision();
    static ShapeCollision tmp2 = new ShapeCollision();

    public static ShapeCollision testCircleVsPolygon(Circle circle, Polygon polygon, ShapeCollision into, boolean flip) {
        into = into == null ? new ShapeCollision() : into.reset();

        List<Vector2> verts = polygon.transformedVertices();

        Vector2 circlePosition = circle.getPosition().cpy();

        float testDistance = Float.MAX_VALUE;
        float distance, closestX = 0.0f, closestY = 0.0f;

        for (int i = 0; i < verts.size(); i++) {
            distance = circlePosition.cpy().sub(verts.get(i)).len2();

            if (distance < testDistance) {
                testDistance = distance;
                closestX = verts.get(i).x;
                closestY = verts.get(i).y;
            }
        }

        Vector2 normalAxis = new Vector2(closestX - circlePosition.x, closestY - circlePosition.y);
        float normalAxisLen = normalAxis.len();
        normalAxis.nor();

        float test = 0.0f;
        float min1 = normalAxis.dot(verts.get(0));
        float max1 = min1;

        for (int j = 1; j < verts.size(); j++) {
            test = normalAxis.dot(verts.get(j));
            if (test < min1) min1 = test;
            if (test > max1) max1 = test;
        }

        float max2 = circle.getTransformedRadius();
        float min2 = circle.getTransformedRadius();
        float offset = normalAxis.dot(circlePosition.cpy().scl(-1));

        min1 += offset;
        max1 += offset;

        float test1 = min1 - max2;
        float test2 = min2 - max1;

        //if either is greater than zero we have a gap.
        if (test1 > 0 || test2 > 0) return null;

        float distMin = -(max2 - min1);
        if (flip) distMin *= -1;

        into.overlap = distMin;
        into.unitVectorX = normalAxis.x;
        into.unitVectorY = normalAxis.y;

        float closest = Math.abs(distMin);

        for (int i = 0; i < verts.size(); i++) {
            normalAxis.x = findNormalAxisX(verts, i);
            normalAxis.y = findNormalAxisY(verts, i);
            normalAxis.nor();

            min1 = normalAxis.dot(verts.get(0));
            max1 = min1;

            for (int j = 1; j < verts.size(); j++) {
                test = normalAxis.dot(verts.get(j));
                if (test < min1) min1 = test;
                if (test > max1) max1 = test;
            }

            max2 = circle.getTransformedRadius();
            min2 = -circle.getTransformedRadius();

            offset = normalAxis.dot(circlePosition.cpy().scl(-1));
            min1 += offset;
            max1 += offset;

            test1 = min1 - max2;
            test2 = min2 - max1;

            if (test1 > 0 || test2 > 0) {
                return null;
            }

            distMin = -(max2 - min1);
            if (flip) distMin *= -1;

            if (Math.abs(distMin) < closest) {
                into.unitVectorX = normalAxis.x;
                into.unitVectorY = normalAxis.y;
                into.overlap = distMin;
                closest = Math.abs(distMin);
            }

        }

        //if we actually get here, we have a collision.
        into.shape1 = flip ? polygon : circle;
        into.shape2 = flip ? circle : polygon;
        into.separationX = into.unitVectorX * into.overlap;
        into.separationY = into.unitVectorY * into.overlap;

        if (!flip) {
            into.unitVectorX = -into.unitVectorX;
            into.unitVectorY = -into.unitVectorY;
        }

        return into;
    }

    public static ShapeCollision testCircleVsCircle(Circle circleA, Circle circleB, ShapeCollision into, boolean flip) {
        Circle circle1 = flip ? circleB : circleA;
        Circle circle2 = flip ? circleA : circleB;

        float totalRadius = circle1.getTransformedRadius() + circle2.getTransformedRadius();

        float distanceSq = circle1.position.cpy().sub(circle2.position).len2();

        if (distanceSq < totalRadius * totalRadius) {
            float difference = totalRadius - circle1.position.cpy().sub(circle2.position).len(); //just use len instead of len2, not square root

            into.shape1 = circle1;
            into.shape2 = circle2;

            Vector2 unitVector = new Vector2(circle1.x - circle2.x, circle1.y - circle2.y);
            unitVector.nor();

            into.unitVectorX = unitVector.x;
            into.unitVectorY = unitVector.y;

            into.separationX = into.unitVectorX * difference;
            into.separationY = into.unitVectorY * difference;
            into.overlap = difference;

            return into;
        }
        return null;
    }

    public static ShapeCollision testPolygonVsPolygon(Polygon polygon1, Polygon polygon2, ShapeCollision into, boolean flip) {
        into = (into == null) ? new ShapeCollision() : into.reset();

        if (checkPolygons(polygon1, polygon2, tmp1, flip) == null) {
            return null;
        }

        if (checkPolygons(polygon2, polygon1, tmp2, !flip) == null) {
            return null;
        }

        ShapeCollision result, other;
        if (Math.abs(tmp1.overlap) < Math.abs(tmp2.overlap)) {
            result = tmp1;
            other = tmp2;
        } else {
            result = tmp2;
            other = tmp1;
        }

        result.otherOverlap = other.overlap;
        result.otherSeparationX = other.separationX;
        result.otherSeparationY = other.separationY;
        result.otherUnitVectorX = other.unitVectorX;
        result.otherUnitVectorY = other.unitVectorY;

        into.copy(result);
        result = other = null;

        return into;
    }

    public static RayCollision testRayVsCircle(Ray ray, Circle circle, RayCollision into) {
        Vector2 delta = new Vector2(ray.end.x - ray.start.x, ray.end.y - ray.start.y);
        Vector2 ray2Circle = new Vector2(ray.start.x - circle.position.x, ray.start.y - circle.position.y);

        float a = delta.len2();
        float b = 2 * delta.dot(ray2Circle);
        float c = ray2Circle.dot(ray2Circle) - circle.getRadius() * circle.getRadius();
        float d = b * b - 4 * a * c;

        if (d >= 0) {

            d = (float) Math.sqrt(d);

            float t1 = (-b - d) / (2 * a);
            float t2 = (-b + d) / (2 * a);

            boolean valid;
            switch (ray.infinite) {
                case NOT_INFINITE:
                    valid = t1 >= 0.0 && t1 <= 1.0;
                    break;
                case INFINITE_FROM_START:
                    valid = t1 >= 0.0;
                    break;
                default:
                    //case INFINITE:
                    valid = true;
            }

            if (valid) {

                into = (into == null) ? new RayCollision() : into.reset();

                into.shape = circle;
                into.ray = ray;
                into.start = t1;
                into.end = t2;

                return into;

            } //

        } //d >= 0

        return null;

    } //testRayVsCircle

    public static RayCollision testRayVsPolygon(Ray ray, Polygon polygon, RayCollision into) {
        float min_u = Float.POSITIVE_INFINITY;
        float max_u = Float.NEGATIVE_INFINITY;

        Vector2 start = ray.start.cpy();
        Vector2 delta = ray.end.cpy().sub(start);

        List<Vector2> verts = polygon.transformedVertices();
        Vector2 v1 = verts.get(verts.size() - 1);
        Vector2 v2 = verts.get(0);

        float ud = (v2.y - v1.y) * delta.x - (v2.x - v1.x) * delta.y;
        float ua = rayU(ud, start.x, start.y, v1.x, v1.y, v2.x - v1.x, v2.y - v1.y);
        float ub = rayU(ud, start.x, start.y, v1.x, v1.y, delta.x, delta.y);

        if (ud != 0.0 && ub >= 0.0 && ub <= 1.0) {
            if (ua < min_u) min_u = ua;
            if (ua > max_u) max_u = ua;
        }

        for (int i = 1; i < verts.size(); i++) {
            v1 = verts.get(i - 1);
            v2 = verts.get(i);

            ud = (v2.y - v1.y) * delta.x - (v2.x - v1.x) * delta.y;
            ua = rayU(ud, start.x, start.y, v1.x, v1.y, v2.x - v1.x, v2.y - v1.y);
            ub = rayU(ud, start.x, start.y, v1.x, v1.y, delta.x, delta.y);

            if (ud != 0.0 && ub >= 0.0 && ub <= 1.0) {
                if (ua < min_u) min_u = ua;
                if (ua > max_u) max_u = ua;
            }
        }

        boolean valid = true;
        switch (ray.infinite) {
            case NOT_INFINITE:
                valid = min_u >= 0.0 && min_u <= 1.0f;
                break;
            case INFINITE_FROM_START:
                valid = min_u != Float.POSITIVE_INFINITY && min_u >= 0.0f;
                break;
            case INFINITE:
                valid = min_u != Float.POSITIVE_INFINITY;
                break;
        }

        if (valid) {
            into = (into == null) ? new RayCollision() : into.reset();
            into.shape = polygon;
            into.ray = ray;
            into.start = min_u;
            into.end = max_u;

            return into;
        }
        return null;
    }

    /*
    public static function testRayVsRay( ray1:Ray, ray2:Ray, ?into:RayIntersection ) : RayIntersection {

        var delta1X = ray1.end.x - ray1.start.x;
        var delta1Y = ray1.end.y - ray1.start.y;
        var delta2X = ray2.end.x - ray2.start.x;
        var delta2Y = ray2.end.y - ray2.start.y;
        var diffX = ray1.start.x - ray2.start.x;
        var diffY = ray1.start.y - ray2.start.y;
        var ud = delta2Y * delta1X - delta2X * delta1Y;

        if(ud == 0.0) return null;

        var u1 = (delta2X * diffY - delta2Y * diffX) / ud;
        var u2 = (delta1X * diffY - delta1Y * diffX) / ud;

            //:todo: ask if ray hit condition difference is intentional (> 0 and not >= 0 like other checks)
        var valid1 = switch(ray1.infinite) {
            case not_infinite: (u1 > 0.0 && u1 <= 1.0);
            case infinite_from_start: u1 > 0.0;
            case infinite: true;
        }

        var valid2 = switch(ray2.infinite) {
            case not_infinite: (u2 > 0.0 && u2 <= 1.0);
            case infinite_from_start: u2 > 0.0;
            case infinite: true;
        }

        if(valid1 && valid2) {

            into = (into == null) ? new RayIntersection() : into.reset();

            into.ray1 = ray1;
            into.ray2 = ray2;
            into.u1 = u1;
            into.u2 = u2;

            return into;

        } //both valid

        return null;

    } //testRayVsRay
     */


    public static float rayU(float uDelta, float aX, float aY, float bX, float bY, float dX, float dY) {
        return (dX * (aY - bY) - dY * (aX - bX)) / uDelta;
    }

    /**
     * This returns normal x axis value. Normal meaning perpendicular, not like the nor() function.
     *
     * @param verts
     * @param index
     * @return
     */
    private static float findNormalAxisX(List<Vector2> verts, int index) {
        Vector2 v2 = (index >= verts.size() - 1) ? verts.get(0) : verts.get(index + 1);
        return -(v2.y - verts.get(index).y);
    }

    private static float findNormalAxisY(List<Vector2> verts, int index) {
        Vector2 v2 = (index >= verts.size() - 1) ? verts.get(0) : verts.get(index + 1);
        return (v2.x - verts.get(index).x);
    }

    public static ShapeCollision checkPolygons(Polygon polygon1, Polygon polygon2, ShapeCollision into, boolean flip) {
        into.reset();

        float offset, test1, test2, testNum;
        float min1, max1, min2, max2;
        float closest = Float.MAX_VALUE;

        float axisX = 0;
        float axisY = 0;

        List<Vector2> verts1 = polygon1.transformedVertices();
        List<Vector2> verts2 = polygon2.transformedVertices();

        for (int i = 0; i < verts1.size(); i++) {
            Vector2 v = new Vector2(findNormalAxisX(verts1, i), findNormalAxisY(verts1, i)).nor();

            axisX = v.x;
            axisY = v.y;

            min1 = v.dot(verts1.get(0));
            max1 = min1;

            for (int j = 1; j < verts1.size(); j++) {
                testNum = v.dot(verts1.get(j));
                if (testNum < min1) min1 = testNum;
                if (testNum > max1) max1 = testNum;
            }

            min2 = v.dot(verts2.get(0));
            max2 = min2;

            for (int j = 1; j < verts2.size(); j++) {
                testNum = v.dot(verts2.get(j));
                if (testNum < min2) min2 = testNum;
                if (testNum > max2) max2 = testNum;
            }

            test1 = min1 - max2;
            test2 = min2 - max1;

            if (test1 > 0 || test2 > 0) return null;

            float distMin = -(max2 - min2);
            if (flip) distMin *= -1;

            if (Math.abs(distMin) < closest) {
                into.unitVectorX = axisX;
                into.unitVectorY = axisY;
                into.overlap = distMin;
                closest = Math.abs(distMin);
            }
        }

        into.shape1 = flip ? polygon2 : polygon1;
        into.shape2 = flip ? polygon1 : polygon2;
        into.separationX = -into.unitVectorX * into.overlap;
        into.separationY = -into.unitVectorY * into.overlap;

        if (flip) {
            into.unitVectorX *= -1;
            into.unitVectorY *= -1;
        }

        return into;
    }

    public static RayIntersection testRayVsRay(Ray ray1, Ray ray2, RayIntersection into) {
        Vector2 delta1 = new Vector2(ray1.end.x - ray1.start.x, ray1.end.y - ray1.start.y);
        Vector2 delta2 = new Vector2(ray2.end.x - ray2.start.x, ray2.end.y - ray2.start.y);
        Vector2 diff = new Vector2(ray1.start.x - ray2.start.x, ray1.start.y - ray2.start.y);
        float ud = delta2.y * delta1.x - delta2.x * delta1.y;

        if (ud == 0.0f) return null;

        float u1 = (delta2.x * diff.y - delta2.y * diff.x) / ud;
        float u2 = (delta1.x * diff.y - delta1.y * diff.x) / ud;

        boolean valid1;
        switch (ray1.infinite) {
            case NOT_INFINITE:
                valid1 = (u1 > 0.0f && u1 <= 1.0);
                break;
            case INFINITE_FROM_START:
                valid1 = u1 > 0.0f;
                break;
            default:
                valid1 = true;
        }

        boolean valid2;
        switch (ray2.infinite) {
            case NOT_INFINITE:
                valid2 = (u2 > 0.0f && u2 <= 1.0f);
                break;
            case INFINITE_FROM_START:
                valid2 = (u2 > 0.0f);
                break;
            default:
                valid2 = true;
        }

        if(valid1 && valid2) {
            into = (into == null) ? new RayIntersection() : into.reset();

            into.ray1 = ray1;
            into.ray2 = ray2;
            into.u1 = u1;
            into.u2 = u2;

            return into;
        }

        return null;
    }
}
