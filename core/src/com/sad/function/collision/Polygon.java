package com.sad.function.collision;

import com.badlogic.gdx.math.Vector2;

//https://github.com/sevdanski/SAT_AS3/blob/master/com/sevenson/geom/sat/shapes/Polygon.as
public class Polygon implements IPolygon{
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
