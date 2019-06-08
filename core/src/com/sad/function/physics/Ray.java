package com.sad.function.physics;

import com.badlogic.gdx.math.Vector2;

public class Ray {
    private Vector2 start,
                    end;

    public Ray() {
        start = new Vector2();
        end = new Vector2();
    }

    public void cast(Vector2 direction, float distance) {
        this.end = start.cpy().add(direction.cpy().scl(direction));
    }

    public Vector2 getStart() {
        return start;
    }

    public void setStart(Vector2 start) {
        this.start = start;
    }

    public Vector2 getEnd() {
        return end;
    }

    public void setEnd(Vector2 end) {
        this.end = end;
    }
}
