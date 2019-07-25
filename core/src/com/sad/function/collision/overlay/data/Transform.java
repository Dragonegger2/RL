package com.sad.function.collision.overlay.data;

import com.badlogic.gdx.math.Vector2;

public class Transform {
    public static final Transform IDENTITY = null; //TODO fill this out.
    public float x = 0;
    public float y = 0;
    public float cost = 1.0f;
    public float sint = 0.0f;

    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;

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
        return (float)Math.atan2(sint, cost);
    }

    public void lerp(Vector2 dp, float da, float alpha, Transform result) {
        result.set(this);
        result.rotateOnly(da * alpha);
        result.translate(dp.x * alpha, dp.y * alpha);
    }

    public void set(Transform transform) {
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

    public void rotate(float theta) {
        float cos = (float)Math.cos(theta);
        float sin = (float)Math.sin(theta);

        // perform an optimized version of matrix multiplication
        float cost = cos * this.cost - sin * this.sint;
        float sint = sin * this.cost + cos * this.sint;
        float x   = cos * this.x - sin * this.y;
        float y   = sin * this.x + cos * this.y;

        // set the new values
        this.cost = cost;
        this.sint = sint;
        this.x   = x;
        this.y   = y;
    }

    public void rotate(float theta, float x, float y) {
        // pre-compute cos/sin of the given angle
        float cos = (float)Math.cos(theta);
        float sin = (float)Math.sin(theta);

        // perform an optimized version of the matrix multiplication:
        // M(new) = inverse(T) * R * T * M(old)
        float cost = cos * this.cost - sin * this.sint;
        float sint = sin * this.cost + cos * this.sint;
        this.cost = cost;
        this.sint = sint;

        float cx = this.x - x;
        float cy = this.y - y;
        this.x = cos * cx - sin * cy + x;
        this.y = sin * cx + cos * cy + y;
    }

    public void rotate(float theta, Vector2 point) {
        rotate(theta, point.x, point.y);
    }

    public void lerp(Transform end, float alpha, Transform result) {
        // interpolate the position
        float x = this.x + alpha * (end.x - this.x);
        float y = this.y + alpha * (end.y - this.y);

        // compute the angle
        // get the start and end rotations
        // its key that these methods use atan2 because
        // it ensures that the angles are always within
        // the range -pi < theta < pi therefore no
        // normalization has to be done
        float rs = this.getRotation();
        float re = end.getRotation();
        // make sure we use the smallest rotation
        // as described in the comments above, there
        // are two possible rotations depending on the
        // direction, we always choose the smaller
        float diff = re - rs;
        if (diff < -Math.PI) diff += Math.PI*2;
        if (diff > Math.PI) diff -= Math.PI*2;
        // interpolate
        // its ok if this method produces an angle
        // outside the range of -pi < theta < pi
        // since the rotate method uses sin and cos
        // which are not bounded
        float a = diff * alpha + rs;

        // set the result transform to the interpolated transform
        // the following performs the following calculations:
        // result.identity();
        // result.rotate(a);
        // result.translate(x, y);

        result.cost = (float)Math.cos(a);
        result.sint = (float)Math.sin(a);
        result.x   = x;
        result.y   = y;
    }

    public void transform(Vector2 vector) {
        float x = vector.x;
        float y = vector.y;

        vector.x = m00 * x + this.m01 * y + this.x;
        vector.y = m10 * x + this.m11 * y + this.y;
    }
}
