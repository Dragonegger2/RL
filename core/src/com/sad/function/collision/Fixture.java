package com.sad.function.collision;

import com.sad.function.collision.filter.Filter;
import com.sad.function.collision.shape.Convex;

import java.util.UUID;

/**
 * Represents a shape.
 * Has it's own UserData object, TransformComponent (in local space), and most importantly a Convex shape.
 */
public class Fixture {
    private final UUID id;
    private final Convex shape;
    private boolean sensor = false;
    private Object userData;
    private String tag;

    protected Filter filter;

    public Fixture(Convex shape) {
        this.shape = shape;
        this.id = UUID.randomUUID();
        this.sensor = false;

        this.tag = tag;
    }

    public Convex getShape() {
        return this.shape;
    }

    public UUID getId() {
        return this.id;
    }

    public boolean isSensor() {
        return this.sensor;
    }

    public void setSensor(boolean flag) {
        this.sensor = flag;
    }

    public Object getUserData() {
        return this.userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    @Override
    public int hashCode() {
        return 17 + id.hashCode();
    }

    public String getTag() { return this.tag; }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
