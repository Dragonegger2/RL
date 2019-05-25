package com.sad.function.system.collision.headbutt.twod.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.headbutt.twod.Shape;

public class Line extends Shape {
    private Vector2 start;
    private Vector2 end;

    public Line(Vector2 start, Vector2 end) {
        this._origin = new Vector2(0f, 0f);
        this.start = start;
        this.end = end;
    }

    @Override
    public Vector2 getOrigin() {
        _origin.x = (start.x + end.x) / 2;
        _origin.y = (start.y + end.y) / 2;
        return _origin;
    }

    @Override
    public void setOrigin(Vector2 origin) {
        float dx = origin.x - _origin.x;
        float dy = origin.y - _origin.y;

        start.x += dx;
        end.x += dx;
        start.y += dy;
        end.y += dy;

        _origin = origin;
    }

    @Override
    public Vector2 support(Vector2 direction) {
        if (start.dot(direction) > end.dot(direction)) {
            return start;
        }

        return end;
    }
}
