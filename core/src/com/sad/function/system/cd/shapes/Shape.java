package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;

import java.util.List;

public abstract class Shape {
    protected List<Vector2> vertices;

    protected Translation translation;

    public Shape() {}
    public Shape(Translation translation) {
        this.translation = translation;
    }

    /**
     The origin / centre of the bodyShape.
     */
    public Vector2 getOrigin() {
        return new Vector2(translation.x, translation.y);
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