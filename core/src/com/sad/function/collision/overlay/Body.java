package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.shape.Shape;

/**
 * Body is managed by it's local space coordinates.
 */
public class Body {
    private Collidable parent;
    private Vector2 position;
    private Shape s;

    public Body(Vector2 position, Shape s) {
        this.position = position;
        this.s = s;

        boolean isSensor = false;
    }
    public Vector2 getPosition() { return position; }
    public void setParent(Collidable collidable) {
        parent = collidable;
    }
    //TODO Copy shape into this.
}
