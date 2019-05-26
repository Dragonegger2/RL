package com.sad.function.system.collision.headbutt.twod;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public abstract class Shape {
    protected Vector2 _origin;
    protected List<Vector2> vertices;
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
    public Vector2 support(Vector2 direction) {
        Vector2 farthestPoint = vertices.get(0);

        for (Vector2 vertex : vertices) {
            float a = direction.dot(vertex);
            float b = direction.dot(farthestPoint);

            if (a > b) {
                farthestPoint = vertex;
            }
        }

        return farthestPoint;
    }
}