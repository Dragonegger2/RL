package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;

/**
 * Body is managed by it's local space coordinates.
 */
public class Body {
    private Collidable parent;
    private Vector2 position;

    private Vector2 getPosition() { return position; }

    public void setParent(Collidable collidable) {
        parent = collidable;
    }
}
