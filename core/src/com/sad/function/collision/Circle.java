package com.sad.function.collision;

import com.badlogic.gdx.math.Vector2;

public class Circle implements IShape {
    private Vector2 position;
    private float radius;

    public Vector2 getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }

    public void setPosition(Vector2 position) { this.position = position; }
    public void setRadius(float radius) { this.radius = radius; }
}
