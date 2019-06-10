package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;

public class Line extends Polygon {
    private Vector2 start;
    private Vector2 end;

    public Line(Vector2 start, Vector2 end) {
        this.vertices = new Vector2[2];
        this.vertices[0] = start;
        this.vertices[1] = end;
        this.start = start;
        this.end = end;
    }

    public Vector2 getStart() {
        return start;
    }

    public void setStart(Vector2 start) {
        this.vertices[0] = start;
        this.start = start;
    }

    public Vector2 getEnd() {

        return end;
    }

    public void setEnd(Vector2 end) {
        this.vertices[1] = end;
        this.end = end;
    }

    @Override
    public Vector2 getOrigin() {
        return null;
    }

    @Override
    public Vector2 support(Vector2 direction) {
        return start.dot(direction) > end.dot(direction) ? start : end;
    }
}
