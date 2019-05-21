package com.sad.function.components;

import com.artemis.Component;
import com.artemis.utils.IntBag;

/**
 * Collision detection component.
 */
public class Collidable extends Component {
    public float width = 32f;
    public float height = 16f;
    public CollisionGroup collisionGroup = CollisionGroup.STATIC;

    public boolean isStatic = false;                //Used to detect only moving objects.
    public IntBag collidedWith = new IntBag();

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public enum CollisionGroup {
        PLAYER,
        STATIC
    }
}
