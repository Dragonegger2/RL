package com.sad.function.collision.overlay.broadphase;

import com.sad.function.collision.overlay.AABB;
import com.sad.function.collision.overlay.filter.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;

import java.util.*;

/**
 * Naive implementation of AbstractBroadphase collision such as discussed here:
 * http://allenchou.net/2013/12/game-physics-broadphase/
 */
public class NSquared extends AbstractBroadphase<Body, BodyFixture> {

    protected final IBroadphaseFilter<Body, BodyFixture> defaultFilter = new BroadphaseFilter();
    public Map<Integer, BruteForceBroadphaseNode<Body, BodyFixture>> map;

    public NSquared() {
        map = new HashMap<>();
    }

    @Override
    public void add(Body collidable, BodyFixture fixture) {
        int key = createKey(collidable, fixture);
        BruteForceBroadphaseNode<Body, BodyFixture> node = this.map.get(key);

        if(node != null) {
            node.updateAABB();
        } else {
            map.put(key, new BruteForceBroadphaseNode<>(collidable, fixture));
        }
    }

    @Override
    public boolean remove(Body collidable, BodyFixture fixture) {
        int key = createKey(collidable, fixture);
        Object n = map.remove(key);

        return n != null;
    }

    @Override
    public void update(Body collidable, BodyFixture fixture) {
        add(collidable, fixture);
    }

    @Override
    public AABB getAABB(Body collidable, BodyFixture fixture) {
        int key = createKey(collidable, fixture);
        BruteForceBroadphaseNode<Body, BodyFixture> n = map.get(key);

        if(n != null) {
            return n.aabb;
        }

        return fixture.getShape().createAABB(collidable.getTransform());
    }

    @Override
    public boolean contains(Body collidable, BodyFixture fixture) {
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
    public List<BroadphasePair<Body, BodyFixture>> detect() {
        return this.detect(defaultFilter);
    }

    @Override
    public List<BroadphasePair<Body, BodyFixture>> detect(IBroadphaseFilter<Body, BodyFixture> filter) {
        List<BroadphasePair<Body, BodyFixture>> pairs = new ArrayList<>();

        Collection<BruteForceBroadphaseNode<Body, BodyFixture>> nodes = this.map.values();

        for(BruteForceBroadphaseNode<Body, BodyFixture> node : nodes ) {
            node.tested = false;
        }

        for (BruteForceBroadphaseNode<Body, BodyFixture> node : map.values()) {
            for(BruteForceBroadphaseNode<Body, BodyFixture> other : map.values()) {
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
