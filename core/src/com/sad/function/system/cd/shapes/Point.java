package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;

/**
 * Class representing a single position.
 */
public class Point extends Shape {
    public Point(Translation translation) {
        super(translation);
    }

    /**
     * The support mapping for a sphere C of radius r and center O is given by SC(d) = O + r * ||d||.
     * @param direction
     * @return
     */
    @Override
    public Vector2 support(Vector2 direction) {
        return new Vector2(translation.x, translation.y);
    }
}
