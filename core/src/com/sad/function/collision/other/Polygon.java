package com.sad.function.collision.other;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.other.IPolygon;

//https://github.com/sevdanski/SAT_AS3/blob/master/com/sevenson/geom/sat/shapes/Polygon.as
public class Polygon implements IPolygon {
    private Vector2 position;

    @Override
    public Vector2[] getVertices() {
        return new Vector2[0];
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }
}
