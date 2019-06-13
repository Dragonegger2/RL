package com.sad.function.collision.differ;

import com.sad.function.collision.differ.data.RayCollision;
import com.sad.function.collision.differ.data.RayIntersection;
import com.sad.function.collision.differ.data.ShapeCollision;
import com.sad.function.collision.differ.shapes.Ray;
import com.sad.function.collision.differ.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Java implementation of Collision:
 * https://github.com/snowkit/differ/tree/master/differ
 */
public class Collision {

    public static ShapeCollision shapeWithShape(Shape shape1, Shape shape2, ShapeCollision into) {
        return shape1.test(shape2, into);
    }

    public static List<ShapeCollision> shapeWithShapes(Shape shape1, List<Shape> shapes, List<ShapeCollision> into) {
        List<ShapeCollision> results = into != null ? clearShapeList(into) : new ArrayList<>(shapes.size());

        for (Shape shape : shapes) {
            ShapeCollision result = shapeWithShape(shape1, shape, null);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }

    public static RayCollision rayWithShape(Ray ray, Shape shape, RayCollision into) {
        return shape.testRay(ray, into);
    }

    public static List<RayCollision> rayWithShapes(Ray ray, List<Shape> shapes, List<RayCollision> into) {
        List<RayCollision> results = into != null ? clearShapeList(into) : new ArrayList<>(shapes.size());

        for (Shape shape : shapes) {
            RayCollision result = rayWithShape(ray, shape, null);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }

    public static RayIntersection rayWithRay(Ray ray1, Ray ray2, RayIntersection into) {
        return SAT2D.testRayVsRay(ray1, ray2, into);
    }
    private static <T> List<T> clearShapeList(List s) {
        s.clear();
        return s;
    }
}
