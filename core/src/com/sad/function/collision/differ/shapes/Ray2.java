package com.sad.function.collision.differ.shapes;

import com.badlogic.gdx.math.Vector2;

public class Ray2 {
    public Vector2 origin;
    public Vector2 direction;
    public InfiniteState infinite;


    public Ray2(Vector2 origin, Vector2 direction, InfiniteState infinite) {
        this.origin = origin.cpy();
        this.direction = direction.cpy();

        this.infinite = infinite == null ? InfiniteState.NOT_INFINITE: infinite;
    }

    public Vector2 getDirection() { return direction; }

    public enum InfiniteState {
        NOT_INFINITE,
        INFINITE_FROM_START,
        INFINITE
    }
}
