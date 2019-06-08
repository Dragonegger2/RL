package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;

import java.util.ArrayList;

public class Polygon extends Shape {
    public Vector2 origin;

    public Polygon() {}
    public Polygon(Vector2 origin, Vector2[] vertices) {
        this.origin = origin;
        this.vertices = vertices;
    }

    public void setVertices(Vector2[] vertices) {
        //TODO Keep an eye on this, may cause issues if I try and reuse shapes.
        this.vertices = vertices;
    }

    @Override
    public Vector2 getOrigin() {
        return origin;
    }
}
