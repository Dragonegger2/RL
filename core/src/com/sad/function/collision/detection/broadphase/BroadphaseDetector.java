package com.sad.function.collision.detection.broadphase;

import com.sad.function.collision.data.AABB;
import com.sad.function.collision.detection.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Fixture;

import java.util.List;

public interface BroadphaseDetector<E extends Collidable<T>, T extends Fixture> {

    static int createKey(Collidable c1, Fixture fixture) {
        int hash = 17;
        hash *= 31 + c1.getId().hashCode();
        hash *= 41 + fixture.getId().hashCode();

        return hash;
    }

    /**
     * Add the provided {@link Collidable} to this implementation of a broadphase detector.
     *
     * @param collidable {@link Collidable} to add to this object.
     */
    void add(E collidable);

    /**
     * Removes all fixtures from this manager.
     *
     * @param collidable to retrieve fixtures from.
     */
    void remove(E collidable);

    /**
     * Updates a collidable's fixtures.
     * THIS METHOD MAY NOT BE NEEDED.
     *
     * @param collidable to update.
     */
    void update(E collidable);

    void add(E collidable, T fixture);

    boolean remove(E collidable, T fixture);

    void update(E collidable, T fixture);

    AABB getAABB(E collidable);

    AABB getAABB(E collidable, T fixture);

    boolean contains(E collidable);

    boolean contains(E collidable, T fixture);

    int size();

    void clear();

    List<BroadphasePair<E, T>> detect();
    List<BroadphasePair<E, T>> detect(BroadphaseFilter<E, T> filter);
    List<BroadphaseItem<E, T>> detect(AABB aabb, BroadphaseFilter<E, T> filter);
    //method to return collision methods for the provided AABB.
}