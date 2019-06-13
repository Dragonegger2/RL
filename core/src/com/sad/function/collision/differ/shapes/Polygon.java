package com.sad.function.collision.differ.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.differ.data.RayCollision;
import com.sad.function.collision.differ.SAT2D;
import com.sad.function.collision.differ.data.ShapeCollision;

import java.util.ArrayList;
import java.util.List;

public class Polygon extends Shape {

    private List<Vector2> _transformedVertices;
    private List<Vector2> _vertices;

    public Polygon(float x, float y, List<Vector2> vertices) {
        super(x, y);

        name = "polygon" + vertices.size();

        _transformedVertices = new ArrayList<>();
        _vertices.addAll(vertices);
    }

    /**
     * Helper function to create a n-gon
     * @param x
     * @param y
     * @param sides
     * @param radius
     * @return
     */
    public static Polygon createNgon(float x, float y, int sides, float radius) {
        if (sides < 3) {
            throw new RuntimeException("Polygon needs at least 3 sides");
        }

        float rotation = (float) (Math.PI * 2) / sides;
        float angle;
        Vector2 vector;
        List<Vector2> vertices = new ArrayList<>();

        for (int i = 0; i < sides; i++) {
            angle = (float) ((i * rotation) + ((Math.PI - rotation) * 0.5f));
            vector = new Vector2();
            vector.x = (float) Math.cos(angle) * radius;
            vector.y = (float) Math.sin(angle) * radius;

            vertices.add(vector.cpy());
        }

        return new Polygon(x, y, vertices);
    }

    public static Polygon rectangle(float x, float y, float width, float height, boolean centered) {
        List<Vector2> vertices = new ArrayList<>();

        if (centered) {

            vertices.add(new Vector2(-width / 2, -height / 2));
            vertices.add(new Vector2(width / 2, -height / 2));
            vertices.add(new Vector2(width / 2, height / 2));
            vertices.add(new Vector2(-width / 2, height / 2));

        } else {

            vertices.add(new Vector2(0, 0));
            vertices.add(new Vector2(width, 0));
            vertices.add(new Vector2(width, height));
            vertices.add(new Vector2(0, height));

        }

        return new Polygon(x, y, vertices);
    }

    public static Polygon square(float x, float y, float width, boolean centered) {
        return rectangle(x, y, width, width, centered);
    }

    public List<Vector2> transformedVertices() {
        return null;
    }

    List<Vector2> getTransformedVertices() {
        if (!_transformed) {
            _transformedVertices = new ArrayList<>();
            _transformed = true;

            for (int i = 0; i < _vertices.size(); i++) {
                _transformedVertices.add(transform(_vertices.get(i), _transformMatrix));
            }
        }

        return _transformedVertices;
    }

    List<Vector2> getVertices() {
        return _vertices;
    }

    @Override
    public ShapeCollision test(Shape shape, ShapeCollision into) {
        return shape.testPolygon(this, into, true);
    }

    @Override
    public ShapeCollision testCircle(Circle circle, ShapeCollision into, boolean flip) {
        return SAT2D.testCircleVsPolygon(circle, this, into, !flip);
    }

    @Override
    public ShapeCollision testPolygon(Polygon polygon, ShapeCollision into, boolean flip) {
        return SAT2D.testPolygonVsPolygon(this, polygon, into, flip);
    }

    @Override
    public RayCollision testRay(Ray ray, RayCollision into) {
        return SAT2D.testRayVsPolygon(ray, this, into);
    }

    @Override
    public void destroy() {
        //Destroy all vertices.
        super.destroy();
    }

}
