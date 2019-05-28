package com.sad.function.components;

import com.artemis.Component;

/**
 * Contains the coordinates of an entity in world space based on the origin of the object.
 *
 * This is specific
 */
public class Translation extends Component {

    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public Translation setX(float x) {
        this.x = x;
        return this;
    }

    public Translation setY(float y) {
        this.y = y;
        return this;
    }

    public Translation setZ(float z) {
        this.z = z;
        return this;
    }
}
