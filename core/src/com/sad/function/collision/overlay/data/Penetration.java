package com.sad.function.collision.overlay.data;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.shape.Shape;

public class Penetration {
    private Vector2 normal;
    private float distance;
    private Shape a;
    private Shape b;
    private boolean aContainsB;
    private boolean bContainsA;

    public Penetration(Vector2 pNro, float overlap, Shape a, Shape b) {
        this.normal = pNro;
        this.distance = overlap;
        this.a = a;
        this.b = b;
    }

    public float getDistance() {
        return distance;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public Shape getShapeA() {
        return a;
    }

    public Shape getShapeB() {
        return b;
    }

    public void setbContainsA(boolean bContainsA) {
        this.bContainsA = bContainsA;
    }
    public boolean doesBContainsA() {
        return bContainsA;
    }

    public void setAContainsB(boolean aContainsB) {
        this.aContainsB = aContainsB;
    }
    public boolean doesAContainsB() {
        return aContainsB;
    }
}
