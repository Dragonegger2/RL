package com.sad.function.collision.overlay.data;

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
}
