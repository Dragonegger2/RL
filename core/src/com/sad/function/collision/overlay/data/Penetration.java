package com.sad.function.collision.overlay.data;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.shape.Shape;

public class Penetration {
    public Vector2 normal;
    public float distance;
    public Shape a;
    public Shape b;

    public Penetration() {}
    public Penetration(Vector2 pNro, float overlap, Shape a, Shape b) {
        this.normal = pNro;
        this.distance = overlap;
        this.a = a;
        this.b = b;
    }

    public Penetration clear() {
        normal = null;
        distance = -1;
        a = null;
        b = null;

        return this;
    }

    public Penetration clone() {
        Penetration clone = new Penetration();
        clone.normal = normal;
        clone.distance = distance;
        clone.a = a;
        clone.b = b;

        return clone;
    }

    public float getDepth() {
        return distance;
    }
}
