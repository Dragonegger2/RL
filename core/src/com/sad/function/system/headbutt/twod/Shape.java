package com.sad.function.system.headbutt.twod;

import com.badlogic.gdx.math.Vector2;

public interface Shape {
    /**
     The origin / centre of the shape.
     */
    Vector2 getOrigin();
    Vector2 setOrigin();

    /**
     Given a direction in global coordinates, return the vertex (in global coordinates)
     that is the furthest in that direction
     @param direction
     @return Vec2
     */
    Vector2 support(Vector2 direction);
}