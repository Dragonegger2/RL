package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;

import java.util.ArrayList;

public class Polygon extends Shape {

    public Polygon(Translation translation, ArrayList<Vector2> vertices) {
        super(translation);
        this.vertices = vertices;
    }

//    @Override
//    public Vector2 getOrigin() {
//        _origin = centerOfPolygon();
//
//        return _origin;
//    }

//    public Vector2 centerOfPolygon() {
//        Vector2 center = new Vector2();
//        for (Vector2 vertex : vertices) {
//            center.add(vertex);
//        }
//
//        center = center.scl(1/(float)(vertices.size()));
//        return center;
//    }
}
