package com.sad.function.collision.differ.shapes;

import com.sad.function.collision.differ.data.RayCollision;
import com.sad.function.collision.differ.data.ShapeCollision;

public class Circle extends Shape {
    private float _radius;

    public Circle(float x, float y, float radius) {
        super(x, y);
        _radius = radius;
        name = "circle" + radius;
    }

    @Override
    public ShapeCollision test(Shape shape, ShapeCollision into) {
        return shape.testCircle(this, into, true);
    }

    @Override
    public ShapeCollision testCircle(Circle circle, ShapeCollision into, boolean flip) {
        return null;
    }

    @Override
    public ShapeCollision testPolygon(Polygon polygon, ShapeCollision into, boolean flip) {
        return null;
    }

    @Override
    public RayCollision testRay(Ray ray, RayCollision into) {
        return null;
    }

    public float getRadius() {
        return _radius;
    }

    public float getTransformedRadius() {
        return _radius * getScaleX();
    }
}
