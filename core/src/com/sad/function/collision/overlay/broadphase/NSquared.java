package com.sad.function.collision.overlay.broadphase;

import com.sad.function.collision.overlay.data.AABB;
import com.sad.function.collision.overlay.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.filter.DetectBroadphaseFilter;

import java.util.*;

/**
 * Naive implementation of AbstractBroadphaseDetector collision such as discussed here:
 * http://allenchou.net/2013/12/game-physics-broadphase/
 */
public class NSquared extends AbstractBroadphaseDetector<Body, BodyFixture> {

    protected final BroadphaseFilter<Body, BodyFixture> defaultFilter = new DetectBroadphaseFilter();
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

    //region Detection Methods

    @Override
    public List<BroadphasePair<Body, BodyFixture>> detect() {
        return this.detect(defaultFilter);
    }
    @Override
    public List<BroadphasePair<Body, BodyFixture>> detect(BroadphaseFilter<Body, BodyFixture> filter) {
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

    @Override
    public List<BroadphaseItem<Body, BodyFixture>> detect(AABB aabb, BroadphaseFilter<Body, BodyFixture> filter) {
        List<BroadphaseItem<Body, BodyFixture>> list = new ArrayList<>();
        Collection<BruteForceBroadphaseNode<Body, BodyFixture>> nodes = map.values();

        for(BruteForceBroadphaseNode<Body, BodyFixture> node : nodes) {
            if(aabb.overlaps(node.aabb)) {
                if(filter.isAllowed(aabb, node.collidable, node.fixture)) {
                    list.add(new BroadphaseItem<>(node.collidable, node.fixture));
                }
            }
        }
        return list;
    }

    //endregion
}
