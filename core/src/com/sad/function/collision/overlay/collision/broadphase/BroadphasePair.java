package com.sad.function.collision.overlay.collision.broadphase;

import com.sad.function.collision.overlay.container.Fixture;

public class BroadphasePair<E extends Collidable<T>, T extends Fixture> {
    private final E collidable1;
    private final T fixture1;
    private final E collidable2;
    private final T fixture2;

    public BroadphasePair(E collidable1, T fixture1, E collidable2, T fixture2) {
        this.collidable1 = collidable1;
        this.fixture1 = fixture1;
        this.collidable2 = collidable2;
        this.fixture2 = fixture2;
    }

    public E getCollidable1() {
        return collidable1;
    }

    public T getFixture1() {
        return fixture1;
    }

    public E getCollidable2() {
        return collidable2;
    }

    public T getFixture2() {
        return fixture2;
    }
}