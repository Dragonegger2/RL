package com.sad.function.physics;

import com.badlogic.gdx.math.Vector2;

public class Ray {
    private Vector2 start,
                    end;

    public Ray() {
        start = new Vector2();
        end = new Vector2();
    }

    public Ray cast(Vector2 direction, float distance) {
        this.end = start.cpy().add(direction.cpy().scl(direction));
        return this;
    }

    public Vector2 getStart() {
        return start;
    }

    public Ray setStart(Vector2 start) {
        this.start = start;
        return this;
    }

    public Vector2 getEnd() {
        return end;
    }
}
