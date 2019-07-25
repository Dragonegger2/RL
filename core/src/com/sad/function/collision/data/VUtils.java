package com.sad.function.collision.data;

import com.badlogic.gdx.math.Vector2;

public class VUtils {
    public static Vector2 left(Vector2 v) {
        float temp = v.x;
        v.x = v.y;
        v.y = -temp;

        return v;
    }

    public static Vector2 right(Vector2 v) {
        float temp = v.x;
        v.x = -v.y;
        v.y = temp;
        return v;
    }

    public static float cross(Vector2 a, Vector2 b) {
        return a.x * b.y - a.y * b.x;
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float distance(Vector2 v1, Vector2 v2) {
        return distance(v1.x, v1.y, v2.x, v2.y);
    }

    public static float distanceSquared(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public static float distanceSquared(Vector2 v1, Vector2 v2) {
        return distanceSquared(v1.x, v1.y, v2.x, v2.y);
    }

    public static Vector2 tripleProduct(Vector2 a, Vector2 b, Vector2 c) {
        Vector2 r = new Vector2();

        float dot = a.x * b.y - b.x * a.y;
        r.x = -c.y * dot;
        r.y = c.x * dot;
        return r;

    }
}
