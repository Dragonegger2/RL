package com.sad.function.collision.overlay.collision.broadphase;

import com.sad.function.collision.overlay.container.Fixture;

public class BroadphaseItem<E extends Collidable<T>, T extends Fixture> {
    private final E collidable;
    private final T fixture;

    public BroadphaseItem(E collidable, T fixture) {
        this.collidable = collidable;
        this.fixture = fixture;
    }

    public E getCollidable() {
        return collidable;
    }

    public T getFixture() {
        return fixture;
    }
}
