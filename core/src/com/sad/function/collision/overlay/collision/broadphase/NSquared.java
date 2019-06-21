package com.sad.function.collision.overlay.collision.broadphase;

import com.sad.function.collision.overlay.collision.filter.DefaultFilter;
import com.sad.function.collision.overlay.collision.AABB;
import com.sad.function.collision.overlay.container.Fixture;
import com.sad.function.collision.overlay.data.Transform;
import javafx.util.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Naive implementation of AbstractBroadphase collision such as discussed here:
 * http://allenchou.net/2013/12/game-physics-broadphase/
 */
public class NSquared<E extends Collidable<T>, T extends Fixture> extends AbstractBroadphase<E, T> {
    protected final IBroadphaseFilter<E, T> defaultFilter = new DefaultFilter<E, T>();
    public Map<Integer, BruteForceBroadphaseNode<E, T>> map;

    public NSquared() {
        map = new HashMap<>();
    }


    @Override
    public void add(E collidable) {
        throw new NotImplementedException();
    }

    @Override
    public void add(E collidable, T fixture) {
        int key = createKey(collidable, fixture);
        BruteForceBroadphaseNode<E, T> node = this.map.get(key);

        if(node != null) {
            node.updateAABB();
        } else {
            map.put(key, new BruteForceBroadphaseNode<>(collidable, fixture));
        }
    }

    @Override
    public boolean remove(E collidable, T fixture) {
        int key = createKey(collidable, fixture);
        Object n = map.remove(key);

        return n != null;
    }

    @Override
    public void update(E collidable, T fixture) {
        add(collidable, fixture);
    }

    @Override
    public AABB getAABB(E collidable, T fixture) {
        int key = createKey(collidable, fixture);
        BruteForceBroadphaseNode<E, T> n = map.get(key);

        if(n != null) {
            return n.aabb;
        }

        return fixture.getShape().createAABB(collidable.getTransform());
    }

    @Override
    public boolean contains(E collidable, T fixture) {
        return map.containsKey(createKey(collidable, fixture));
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public List<BroadphasePair<E, T>> detect(IBroadphaseFilter<E, T> filter) {
        List<BroadphasePair<E, T>> pairs = new ArrayList<>();

        Collection<BruteForceBroadphaseNode<E, T>> nodes = this.map.values();

        for(BruteForceBroadphaseNode<E, T> node : nodes ) {
            node.tested = false;
        }

        for (BruteForceBroadphaseNode<E, T> node : map.values()) {
            for(BruteForceBroadphaseNode<E, T> other : map.values()) {
                if(node.aabb.overlaps(other.aabb) && !other.tested && other.collidable != node.collidable) {
                    if(filter.isAllowed(node.collidable, node.fixture, other.collidable, other.fixture)) {
                        pairs.add(new BroadphasePair<>(node.collidable, node.fixture, other.collidable, other.fixture));
                    }
                }
            }

            node.tested = true;
        }

        return pairs;
    }
}
