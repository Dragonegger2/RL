package com.sad.function.system.collision.shapes;

import com.badlogic.gdx.math.Vector2;

public class Line extends Shape {
    private Vector2 start;
    private Vector2 end;

    public Line(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Vector2 support(Vector2 direction) {
        if (start.dot(direction) > end.dot(direction)) {
            return start;
        }

        return end;
    }
}
