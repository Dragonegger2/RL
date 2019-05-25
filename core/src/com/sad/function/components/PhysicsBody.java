package com.sad.function.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Holds the data for my box2d collision detection.
 *
 * The height and width are the size of the body in extant form (halfsize).
 *
 * If you are a circle, store the radius in width.
 */
public class PhysicsBody extends Component {
    public Body body;

    public Vector2 position;
    private float width = 0f;
    private float height = 0f;
    public float density = 1.0f;

    public BodyShape shape = BodyShape.RECTANGLE;

    public PhysicsBody() {
        position = new Vector2();
    }

    /**
     * If the shape is a circle, return the width as that is an alias for radius.
     * @return height in halfsize.
     */
    public float getHeight() {
        if(shape == BodyShape.CIRCLE) {
            return width;
        }

        return height;
    }

    /**
     * Returns the width.
     * @return width of the body in halfsize.
     */
    public float getWidth() {
        return width;
    }

    public PhysicsBody setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * If this body is a circle, update width instead of the height.
     * @param height to assign to this shape.
     * @return this for chaining.
     */
    public PhysicsBody setHeight(float height) {
        if(shape == BodyShape.CIRCLE) {
            this.width = height;
        } else {
            this.height = height;
        }

        return this;
    }

    public enum BodyShape {
        CIRCLE,
        RECTANGLE,
        POLYGON
    }
}
