package com.sad.function.collision.overlay.collision.filter;

import com.badlogic.gdx.math.collision.Ray;
import com.sad.function.collision.overlay.collision.AABB;
import com.sad.function.collision.overlay.collision.broadphase.IBroadphaseFilter;
import com.sad.function.collision.overlay.collision.broadphase.Collidable;
import com.sad.function.collision.overlay.container.Fixture;

/**
 * Returns true for all instances of collidables.
 * @param <E>
 * @param <T>
 */
public class DefaultFilter<E extends Collidable<T>, T extends Fixture> implements IBroadphaseFilter<E, T> {
    public boolean isAllowed(E collidable1, T fixture1, E collidable2, T fixture2) { return true; }

    public boolean isAllowed(AABB aabb, E collidable, T fixture) {
        return true;
    }

    public boolean isAllowed(Ray ray, float length, E collidable, T fixture) {
        return true;
    }
}
