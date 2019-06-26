package com.sad.function.collision.overlay.collision.broadphase;

import com.sad.function.collision.overlay.data.AABB;
import com.sad.function.collision.overlay.collision.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Fixture;

import java.util.List;

public abstract class AbstractBroadphaseDetector<E extends Collidable<T>, T extends Fixture> implements BroadphaseDetector<E,T> {
    protected final BroadphaseFilter<E, T> defaultFilter = new DefaultBroadphaseFilter<>();

    /**
     * Add the provided {@link Collidable} to this implementation of a broadphase detector.
     * @param collidable{@link Collidable} to add to this object.
     */
    @Override
    public void add(E collidable) {
        int size = collidable.getFixtureCount();
        for(int i = 0; i < size; i++) {
            T f = collidable.getFixture(i);
            this.add(collidable, f);
        }
    }

    /**
     * Removes all fixtures from this manager.
     * @param collidable to retrieve fixtures from.
     */
    @Override
    public void remove(E collidable) {
        int size = collidable.getFixtureCount();
        for(int i = 0; i < size; i++) {
            T fixture = collidable.getFixture(i);
            remove(collidable, fixture);
        }
    }

    /**
     * Updates a collidable's fixtures.
     * @param collidable to update.
     */
    @Override
    public void update(E collidable) {
        int size = collidable.getFixtureCount();
        for(int i = 0; i < size; i++) {
            T f = collidable.getFixture(i);
            update(collidable, f);
        }
    }

    @Override
    public AABB getAABB(E collidable) {
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
    public boolean contains(E collidable) {
        int size = collidable.getFixtureCount();
        for(int i = 0; i < size; i++) {
            T f = collidable.getFixture(i);
            if(!contains(collidable,f)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<BroadphasePair<E, T>> detect() {
        return detect(this.defaultFilter);
    }

    public abstract void add(E collidable, T fixture);
    public abstract boolean remove(E collidable, T fixture);
    public abstract void update(E collidable, T fixture);
    public abstract AABB getAABB(E collidable, T fixture);
    public abstract boolean contains(E collidable, T fixture);

    public abstract int size();
    public abstract void clear();

    static int createKey(Collidable c1, Fixture fixture) {
        int hash = 17;
        hash *= 31 + c1.getId().hashCode();
        hash *= 41 + fixture.getId().hashCode();

        return hash;
    }
}