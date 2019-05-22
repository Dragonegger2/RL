package com.sad.function.components;

import com.artemis.Component;
import com.artemis.utils.IntBag;

/**
 * Collision detection component.
 */
public class Collidable extends Component {
    public float width = 32f;
    public float height = 16f;

    public boolean isStatic = false;                //Used to detect only moving objects.
    public IntBag collidedWith = new IntBag();

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Collidable setHeight(float height) {
        this.height = height;
        return this;
    }

    public Collidable setWidth(float width) {
        this.width = width;
        return this;
    }

    public Collidable setIsState(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }
}
