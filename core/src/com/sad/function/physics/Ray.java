package com.sad.function.physics;

import com.badlogic.gdx.math.Vector2;

public class Ray {
    //Direction should be a normalized vector.
    private Vector2 direction;
    private Vector2 origin;

    public Ray() {
        origin = new Vector2();
        direction = new Vector2();
    }

    public Ray setDirection(Vector2 direction) {
        this.direction = direction;
        return this;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public Ray setOrigin(Vector2 origin) {
        this.origin = origin.cpy();
        return this;
    }

    public Vector2 getDirection() {
        return direction;
    }
}
