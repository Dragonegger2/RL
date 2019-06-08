package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;

public class Circle extends Shape {
    public float radius;

    private Vector2 origin;

    public Circle(Vector2 origin, float radius) {
        this.origin = origin;
        this.radius = radius;
    }

    @Override
    public Vector2 getOrigin() {
        return origin;
    }

    /**
     * The support mapping for a sphere C of radius r and center O is given by SC(d) = O + r * ||d||.
     * @param direction
     * @return
     */
    @Override
    public Vector2 support(Vector2 direction) {
        Vector2 c = new Vector2(origin.x, origin.y);
        Vector2 d = new Vector2(direction).nor();
        d.scl(radius);
        c.add(d);

        return c;
    }
}
