package com.sad.function.collision.overlay.container;

import com.sad.function.collision.overlay.shape.Convex;

import java.util.UUID;

/**
 * Fixture is managed by it'shape local space coordinates.
 */
public class Fixture {
    protected final UUID id;
    protected final Convex shape;
    protected boolean sensor;

    /**
     * Fixture without offset.
     *
     * @param shape of this fixture.
     */
    public Fixture(Convex shape) {
        if (shape == null) throw new NullPointerException("Shapes being past into fixtures can't be null");
        this.id = UUID.randomUUID();
        this.shape = shape;
        //this.filter = Filter.DEFAULT_FILTER;
        this.sensor = false;
    }

    public UUID getId() {
        return this.id;
    }

    public Convex getShape() {
        return this.shape;
    }

    public boolean isSensor() {
        return this.sensor;
    }

    public void setSensor(boolean flag) {
        this.sensor = flag;
    }
}
