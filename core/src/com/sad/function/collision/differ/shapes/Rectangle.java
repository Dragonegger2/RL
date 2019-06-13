package com.sad.function.collision.differ.shapes;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends Polygon {
    private Vector2 halfsize;

    public Rectangle(float x, float y, float width, float height, boolean centered) {
        super(x, y, createVertices(width, height, centered));

        halfsize = new Vector2(width, height);
    }

    private static List<Vector2> createVertices(float width, float height, boolean centered) {
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

        return vertices;
    }

    public float getWidth() {
        return halfsize.x;
    }

    public float getHeight() {
        return halfsize.y;
    }
}
