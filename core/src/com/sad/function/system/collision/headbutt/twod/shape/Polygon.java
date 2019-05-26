package com.sad.function.system.collision.headbutt.twod.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.collision.headbutt.twod.Shape;

import java.util.ArrayList;

public class Polygon extends Shape {

    public Polygon(ArrayList<Vector2> vertices) {
        this.vertices = vertices;
    }

    @Override
    public Vector2 getOrigin() {
        _origin = centerOfPolygon();

        return _origin;
    }

    public Vector2 centerOfPolygon() {
        Vector2 center = new Vector2();
        for (Vector2 vertex : vertices) {
            center.add(vertex);
        }

        center = center.scl(1/(float)(vertices.size()));
        return center;
    }
}
