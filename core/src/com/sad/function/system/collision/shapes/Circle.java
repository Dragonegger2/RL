package com.sad.function.system.collision.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;

public class Circle extends Shape {
    private float radius;

    public Circle(Translation translation, float radius) {
        super(translation);
        this.radius = radius;
    }

    /**
     * The support mapping for a sphere C of radius r and center O is given by SC(d) = O + r * ||d||.
     * @param direction
     * @return
     */
    @Override
    public Vector2 support(Vector2 direction) {
        Vector2 c = new Vector2(translation.x, translation.y);
        Vector2 d = new Vector2(direction).nor();
        d.scl(radius);
        c.add(d);

        return c;
    }
}
