package com.sad.function.components;

import com.artemis.Component;

/**
 * Collision detection component.
 */
public class Collidable extends Component {
    public float width = 32f;
    public float height = 16f;
    public float xOffset = 0f;
    public float yOffset = 0f;

    public boolean isStatic = false;                //Used to detect only moving objects.

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

    public Collidable setXOffset(float xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    public Collidable setYOffset(float yOffset) {
        this.yOffset = yOffset;
        return this;
    }
}
