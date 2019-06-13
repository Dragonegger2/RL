package com.sad.function.collision.differ.shapes;

import com.badlogic.gdx.math.Vector2;

public class Ray {
    public Vector2 start;
    public Vector2 end;
    public InfiniteState infinite;

    private Vector2 dir_cache;

    public Ray(Vector2 start, Vector2 end, InfiniteState infinite) {
        this.start = start.cpy();
        this.end = end.cpy();
        this.infinite = infinite == null ? InfiniteState.NOT_INFINITE: infinite;

        dir_cache = new Vector2(end.x-start.x, end.y - start.y);
    }

    public Vector2 getDir() {
        dir_cache.x = end.x - start.x;
        dir_cache.y = end.y - start.y;

        return dir_cache;
    }

    public enum InfiniteState {
        NOT_INFINITE,
        INFINITE_FROM_START,
        INFINITE
    }
}