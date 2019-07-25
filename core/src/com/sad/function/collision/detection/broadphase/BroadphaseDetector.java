package com.sad.function.collision.detection.broadphase;

import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.AABB;
import com.sad.function.collision.detection.broadphase.filters.BroadphaseFilter;

import java.util.List;

public interface BroadphaseDetector {

    static int createKey(Body c1, Fixture fixture) {
        int hash = 17;
        hash *= 31 + c1.getId().hashCode();
        hash *= 41 + fixture.getId().hashCode();

        return hash;
    }

    /**
     * Add the provided {@link Body} to this implementation of a broadphase detector.
     *
     * @param collidable {@link Body} to add to this object.
     */
    void add(Body collidable);

    /**
     * Removes all fixtures from this manager.
     *
     * @param collidable to retrieve fixtures from.
     */
    void remove(Body collidable);

    /**
     * Updates a collidable's fixtures.
     * THIS METHOD MAY NOT BE NEEDED.
     *
     * @param collidable to update.
     */
    void update(Body collidable);

    void add(Body collidable, Fixture fixture);

    boolean remove(Body collidable, Fixture fixture);

    void update(Body collidable, Fixture fixture);

    AABB getAABB(Body collidable);

    AABB getAABB(Body collidable, Fixture fixture);

    boolean contains(Body collidable);

    boolean contains(Body collidable, Fixture fixture);

    int size();

    void clear();

    List<BroadphasePair> detect();
    List<BroadphasePair> detect(BroadphaseFilter filter);
    List<BroadphaseItem> detect(AABB aabb, BroadphaseFilter filter);
    //method to return collision methods for the provided AABB.
}