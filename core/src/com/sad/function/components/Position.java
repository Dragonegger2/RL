package com.sad.function.components;

import com.badlogic.ashley.core.Component;

/**
 * Contains the coordinates of an entity in world space based on the center of the object.
 */
public class Position extends com.artemis.Component implements Component {

    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public Position setX(float x) {
        this.x = x;
        return this;
    }

    public Position setY(float y) {
        this.y = y;
        return this;
    }

    public Position setZ(float z) {
        this.z = z;
        return this;
    }
}
