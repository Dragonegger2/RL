package com.sad.function.system.collision.headbutt.twod.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.headbutt.twod.Shape;

public class Circle extends Shape {
    private float radius;

    public Circle(Vector2 origin, float radius) {
        this._origin = origin;
        this.radius = radius;
    }

    @Override
    public Vector2 support(Vector2 direction) {
        Vector2 c = new Vector2(_origin);
        Vector2 d = new Vector2(direction).nor();
        d.scl(radius);
        c.add(d);

        return c;
    }
}
