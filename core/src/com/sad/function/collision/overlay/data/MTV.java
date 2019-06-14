package com.sad.function.collision.overlay.data;

import com.badlogic.gdx.math.Vector2;

public class MTV {
    private Vector2 pNor;
    private float distance;

    public MTV(Vector2 pNro, float overlap) {
        this.pNor = pNro;
        this.distance = overlap;
    }

    public float getDistance() {
        return distance;
    }

    public Vector2 getPenetrationVector() {
        return pNor;
    }
}
