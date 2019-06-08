package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;

/**
 * Class representing a single origin.
 */
public class Point extends Shape {
    private Vector2 origin;
    public Point(Vector2 origin) {
        this.origin = origin;
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
        return origin.cpy();
    }
}
