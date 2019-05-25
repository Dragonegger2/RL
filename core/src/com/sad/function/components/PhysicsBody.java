package com.sad.function.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sad.function.system.collision.headbutt.twod.Shape;

/**
 * Holds the data for my box2d collision detection.
 * <p>
 * The height and width are the size of the body in extant form (halfsize).
 * <p>
 * If you are a circle, store the radius in width.
 */
public class PhysicsBody extends Component {
    public Body body;

    public Vector2 position;
    public float density = 1.0f;
    public BodyShape bodyShape = BodyShape.RECTANGLE;
    public Shape hitBox;
    private float width = 0f;
    private float height = 0f;

    public PhysicsBody() {
        position = new Vector2();
    }

    /**
     * If the bodyShape is a circle, return the width as that is an alias for radius.
     *
     * @return height in halfsize.
     */
    public float getHeight() {
        if (bodyShape == BodyShape.CIRCLE) {
            return width;
        }

        return height;
    }

    /**
     * If this body is a circle, update width instead of the height.
     *
     * @param height to assign to this bodyShape.
     * @return this for chaining.
     */
    public PhysicsBody setHeight(float height) {
        if (bodyShape == BodyShape.CIRCLE) {
            this.width = height;
        } else {
            this.height = height;
        }

        return this;
    }

    /**
     * Returns the width.
     *
     * @return width of the body in halfsize.
     */
    public float getWidth() {
        return width;
    }

    public PhysicsBody setWidth(float width) {
        this.width = width;
        return this;
    }

    public enum BodyShape {
        CIRCLE,
        RECTANGLE,
        POLYGON
    }
}
