package com.sad.function.collision.detection.broadphase;

import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.AABB;
import com.sad.function.collision.detection.broadphase.filters.BroadphaseFilter;

import java.util.List;


public abstract class AbstractBroadphaseDetector implements BroadphaseDetector {
    protected final BroadphaseFilter defaultFilter = new DefaultBroadphaseFilter();

    /**
     * Add the provided {@link Body} to this implementation of a broadphase detector.
     * @param collidable {@link Body} to add to this object.
     */
    @Override
    public void add(Body collidable) {
        int size = collidable.getFixtureCount();
        for(int i = 0; i < size; i++) {
            Fixture f = collidable.getFixtures().get(i);
            this.add(collidable, f);
        }
    }

    /**
     * Removes all fixtures from this manager.
     * @param collidable to retrieve fixtures from.
     */
    @Override
    public void remove(Body collidable) {
        int size = collidable.getFixtureCount();
        for(int i = 0; i < size; i++) {
            Fixture fixture = collidable.getFixture(i);
            remove(collidable, fixture);
        }
    }

    /**
     * Updates a collidable's fixtures.
     * @param collidable to update.
     */
    @Override
    public void update(Body collidable) {
        int size = collidable.getFixtureCount();
        for(int i = 0; i < size; i++) {
            Fixture f = collidable.getFixture(i);
            update(collidable, f);
        }
    }

    @Override
    public AABB getAABB(Body collidable) {
        int size = collidable.getFixtureCount();
        if (size == 0) return new AABB(0, 0, 0, 0);
        AABB union = this.getAABB(collidable, collidable.getFixture(0));
        for (int i = 1; i < size; i++) {
            AABB aabb = this.getAABB(collidable, collidable.getFixture(i));
            union.union(aabb);
        }
        return union;
    }

    @Override
    public boolean contains(Body collidable) {
        int size = collidable.getFixtureCount();
        for(int i = 0; i < size; i++) {
            Fixture f = collidable.getFixture(i);
            if(!contains(collidable,f)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<BroadphasePair> detect() {
        return detect(this.defaultFilter);
    }

    public abstract void add(Body collidable, Fixture fixture);
    public abstract boolean remove(Body collidable, Fixture fixture);
    public abstract void update(Body collidable, Fixture fixture);
    public abstract AABB getAABB(Body collidable, Fixture fixture);
    public abstract boolean contains(Body collidable, Fixture fixture);

    public abstract int size();
    public abstract void clear();

    static int createKey(Body c1, Fixture fixture) {
        int hash = 17;
        hash *= 31 + c1.getId().hashCode();
        hash *= 41 + fixture.getId().hashCode();

        return hash;
    }
}