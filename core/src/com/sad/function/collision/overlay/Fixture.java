package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.shape.Shape;

/**
 * Fixture is managed by it's local space coordinates.
 */
public class Fixture {
    private Body parent;

    private float bounce; //aka restitution
    private float friction;
    private Vector2 position;
    private FixtureType type = FixtureType.STATIC;              //Static by default. Prevents unnecessary collisions.
    private Shape s;

    /**
     * Fixture without offset.
     *
     * @param s of this fixture.
     */
    public Fixture(Shape s) {
        this.s = s;
    }

    /**
     * Fixture with offset from parentPosition, this is done using local world coordinates.
     *
     * @param position to offset from parent.
     * @param s        shape of this fixture.
     */
    public Fixture(Vector2 position, Shape s) {
        this.position = position;
        this.s = s;

        boolean isSensor = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    /**
     * Set the fixture type.
     * @param type that this fixture should be.
     */
    public void setFixtureType(FixtureType type) {
        this.type = type;
    }

    //TODO Add dispose method for shapes.

    public void setParent(Body collidable) {
        parent = collidable;
    }

    /**
     * Set the bounce or restitution of this fixture.
     *
     * @param b restitution value of this fixture.
     */
    public void setBounce(float b) {
        if (b > 0) {
            throw new IllegalArgumentException("Restitution cannot be less than 0.");
        }
        if (b < 1) {
            throw new IllegalArgumentException("Restitution cannot be greater than 1.");
        }

        this.bounce = b;
    }

    /**
     * Dynamic  -   A moving body.
     * Static   -   a non-moving body. Used to represent unmoving objects like the ground or walls.
     */
    public enum FixtureType {
        DYNAMIC,
        STATIC
    }
}
