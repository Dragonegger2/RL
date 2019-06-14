package com.sad.function.collision.overlay.data;

import com.badlogic.gdx.math.Vector2;

public class Penetration {
    private Vector2 normal;
    private float distance;

    public Penetration(Vector2 pNro, float overlap) {
        this.normal = pNro;
        this.distance = overlap;
    }

    public float getDistance() {
        return distance;
    }

    public Vector2 getNormal() {
        return normal;
    }
}
