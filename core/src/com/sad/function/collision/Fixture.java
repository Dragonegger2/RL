package com.sad.function.collision;

import com.sad.function.collision.filter.Filter;
import com.sad.function.collision.shape.Convex;

import java.util.HashMap;
import java.util.Map;
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
    protected Map<UUID, Fixture> contact;

    public Fixture(Convex shape) {
        this.shape = shape;
        this.id = UUID.randomUUID();
        this.sensor = false;

        contact = new HashMap<>();
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

    /**
     * Add a {@link Fixture} that this {@link Fixture} is currently in contact with.
     * @param f to add to the contact list.
     */
    public void addContact(Fixture f) {
        contact.put(f.id, f);
    }

    /**
     * Remove the {@link Fixture} that is passed in from this contact list.
     * @param f that this is {@link Fixture} is in contact with.
     * @return the {@link Fixture} that was removed. Returns null if there is no contact to remove.
     */
    public Fixture removeContact(Fixture f) {
        return contact.remove(f.id);
    }

    /**
     * Returns the number of {@link Fixture}s that this object is in contact with.
     * @return int, number of {@link Fixture} that this object is in contact with.
     */
    public int contactCount() { return contact.size(); }
}
