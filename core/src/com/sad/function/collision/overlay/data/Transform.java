package com.sad.function.collision.overlay.data;

import com.badlogic.gdx.math.Vector2;

public class Transform {
    public static final Transform IDENTITY = null; //TODO fill this out.
    public float x = 0;
    public float y = 0;
    public float cost = 1.0f;
    public float sint = 0.0f;

    public Transform() {
    }

    public Transform(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Transform(Vector2 xy) {
        this.x = xy.x;
        this.y = xy.y;
    }

    /**
     * Atomically safe, does not modify the source.
     *
     * @param v
     * @return
     */
    public Vector2 getTransformed(Vector2 v) {
        Vector2 tv = new Vector2();
        float x = v.x;
        float y = v.y;

        tv.x = x + this.x;
        tv.y = y + this.y;

        return tv;
    }

    public Vector2 getTransformedR(Vector2 vector) {
        Vector2 v = new Vector2();
        float x = vector.x;
        float y = vector.y;
        v.x = this.cost * x - this.sint * y;
        v.y = this.sint * x + this.cost * y;
        return v;
    }

    public Vector2 getInverseTransformedR(Vector2 vector) {
        Vector2 v = new Vector2();
        float x = vector.x;
        float y = vector.y;
        // since the transpose of a rotation matrix is the inverse
        v.x = this.cost * x + this.sint * y;
        v.y = -this.sint * x + this.cost * y;
        return v;
    }

    public Vector2 getInverseTransformed(Vector2 vector) {
        Vector2 tv = new Vector2();
        float tx = vector.x - this.x;
        float ty = vector.y - this.y;
        tv.x = this.cost * tx + this.sint * ty;
        tv.y = -this.sint * tx + this.cost * ty;
        return tv;
    }

    public void translate(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void translate(Vector2 vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public void lerp(Transform end, float alpha) {
        float x = this.x + alpha * (end.x - this.x);
        float y = this.y + alpha * (end.y - this.y);

        float rs = this.getRotation();
        float re = end.getRotation();

        float diff = re - rs;
        if (diff < -Math.PI) diff += Math.PI * 2;
        if (diff > Math.PI) diff -= Math.PI * 2;

        float a = diff * alpha + rs;

        this.cost = (float) Math.cos(a);
        this.sint = (float) Math.sin(a);
        this.x = x;
        this.y = y;
    }

    private float getRotation() {
        return 0;
    }

    public void lerp(Vector2 dp, float da, float alpha, Transform result) {
        result.set(this);
        result.rotateOnly(da * alpha);
        result.translate(dp.x * alpha, dp.y * alpha);
    }

    private void set(Transform transform) {
        this.cost = transform.cost;
        this.sint = transform.sint;
        this.x = transform.x;
        this.y = transform.y;
    }

    private void rotateOnly(float theta) {
        float cos = (float)Math.cos(theta);
        float sin = (float)Math.sin(theta);

        float cost = cos * this.cost - sin * this.sint;
        float sint = sin * this.cost + cos * this.sint;

        this.cost = cost;
        this.sint = sint;

    }
}