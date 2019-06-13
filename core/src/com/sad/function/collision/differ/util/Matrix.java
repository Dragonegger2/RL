package com.sad.function.collision.differ.util;

import com.badlogic.gdx.math.Vector2;

public class Matrix {
    public float a;
    public float b;
    public float c;
    public float d;
    public float tx;
    public float ty;

    private float _lastRotation = 0;

    public Matrix() {
        a = 1;
        b = 0;
        c = 0;
        d = 1;
        tx = 0;
        ty = 0;
    }

    public Matrix(float a, float b, float c, float d, float tx, float ty) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.tx = tx;
        this.ty = ty;
    }

    public void identity() {
        a = 1;
        b = 0;
        c = 0;
        d = 1;
        tx = 0;
        ty = 0;
    }

    public void translate(float x, float y) {
        tx += x;
        ty += y;
    }

    public void compose(Vector2 position, float rotation, Vector2 scale) {
        identity();

        scale(scale.x, scale.y);
        rotate(rotation);
        makeTranslation(position.x, position.y);
    }

    public Matrix makeTranslation(float x, float y) {
        tx = x;
        ty = y;

        return this;
    }

    public void rotate(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        float a1 = a * cos - b * sin;
        b = a * sin + b * cos;
        a = a1;

        float c1 = c * cos - d * sin;
        d = c * sin + d * cos;
        c = c1;

        float tx1 = tx * cos - ty * sin;
        ty = tx * sin + ty * cos;
        tx = tx1;
    }

    public void scale(float x, float y) {
        a *= x;
        b *= y;

        c *= x;
        d *= y;

        tx *= x;
        ty *= y;
    }

    @Override
    public String toString() {
        return "(a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", tx=" + tx + ", ty=" + ty + ")";
    }
}
