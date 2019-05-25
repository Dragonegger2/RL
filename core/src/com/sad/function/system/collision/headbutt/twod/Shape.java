package com.sad.function.system.collision.headbutt.twod;

import com.badlogic.gdx.math.Vector2;

public abstract class Shape {
    protected Vector2 _origin;

    /**
     The origin / centre of the bodyShape.
     */
    public Vector2 getOrigin() {
        return _origin;
    }

    public void setOrigin(Vector2 newOrigin) {
        this._origin = newOrigin;
    }

    /**
     Given a direction in global coordinates, return the vertex (in global coordinates)
     that is the furthest in that direction
     @param direction
     @return Vec2
     */
    public abstract Vector2 support(Vector2 direction);
}