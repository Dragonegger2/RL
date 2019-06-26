package com.sad.function.collision.overlay.broadphase;

import com.sad.function.collision.overlay.Collidable;
import com.sad.function.collision.overlay.data.AABB;
import com.sad.function.collision.overlay.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Fixture;
import com.sad.function.collision.overlay.data.Transform;
import org.dyn4j.BinarySearchTree;
import org.dyn4j.collision.Collisions;

import java.util.*;

public class Sap<E extends Collidable<T>, T extends Fixture> extends AbstractBroadphaseDetector<E, T> implements BroadphaseDetector<E, T> {
    BinarySearchTree<SapProxy<E, T>> tree;
    Map<Integer, SapProxy<E, T>> map;
    float expansion = 0;

    public Sap() {
        this(64);
    }

    public Sap(int initialSize) {
        this.tree = new BinarySearchTree<>(true);
        map = new HashMap<>(initialSize * 4 / 3 + 1, 0.75f);
    }

    @Override
    public void add(E collidable, T fixture) {
        int key = createKey(collidable, fixture);
        SapProxy<E, T> proxy = this.map.get(key);
        if (proxy == null) {
            this.add(key, collidable, fixture);
        } else {
            this.update(key, proxy, collidable, fixture);
        }
    }

    private void add(int key, E collidable, T fixture) {
        Transform tx = collidable.getTransform();
        AABB aabb = fixture.getShape().createAABB(tx);
        // expand the aabb
        aabb.expand(this.expansion);
        // create a new node for the collidable
        SapProxy<E, T> proxy = new SapProxy<E, T>(collidable, fixture, aabb);
        // add the proxy to the map
        this.map.put(key, proxy);
        // insert the node into the tree
        this.tree.insert(proxy);
    }

    @Override
    public boolean remove(E collidable, T fixture) {
        int key = createKey(collidable, fixture);
        SapProxy<E, T> proxy = map.remove(key);
        if (proxy != null) {
            tree.remove(proxy);
            return true;
        }

        return false;
    }

    @Override
    public void update(E collidable, T fixture) {
        int key = createKey(collidable, fixture);
        SapProxy<E, T> proxy = this.map.get(key);
        if (proxy != null) {
            this.update(key, proxy, collidable, fixture);
        } else {
            this.add(key, collidable, fixture);
        }
    }

    private void update(int key, SapProxy<E, T> proxy, E collidable, T fixture) {
        Transform tx = collidable.getTransform();
        // create the new aabb
        AABB aabb = fixture.getShape().createAABB(tx);
        // see if the old aabb contains the new one
        if (proxy.aabb.contains(aabb)) {
            // if so, don't do anything
            return;
        }
        // otherwise expand the new aabb
        aabb.expand(this.expansion);
        // remove the current proxy from the tree
        this.tree.remove(proxy);
        // set the new aabb
        proxy.aabb = aabb;
        // reinsert the proxy
        this.tree.insert(proxy);
    }

    @Override
    public AABB getAABB(E collidable, T fixture) {
        int key = createKey(collidable, fixture);
        SapProxy<E, T> proxy = map.get(key);
        if (proxy != null) {
            return proxy.aabb;
        }
        return fixture.getShape().createAABB(collidable.getTransform());
    }

    @Override
    public boolean contains(E collidable, T fixture) {
        int key = createKey(collidable, fixture);
        return map.containsKey(key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
        tree.clear();
    }

    @Override
    public List<BroadphasePair<E, T>> detect(BroadphaseFilter<E, T> filter) {
        // get the number of proxies
        int size = this.tree.size();

        // check the size
        if (size == 0) {
            // return the empty list
            return Collections.emptyList();
        }

        // the estimated size of the pair list
        int eSize = Collisions.getEstimatedCollisionPairs(size);
        List<BroadphasePair<E, T>> pairs = new ArrayList<>(eSize);

        // clear the tested flags
        Iterator<SapProxy<E, T>> itp = this.tree.iterator();
        while (itp.hasNext()) {
            SapProxy<E, T> p = itp.next();
            p.tested = false;
        }

        // find all the possible pairs O(n*log(n))
        Iterator<SapProxy<E, T>> ito = this.tree.iterator();
        while (ito.hasNext()) {
            // get the current proxy
            SapProxy<E, T> current = ito.next();
            Iterator<SapProxy<E, T>> iti = this.tree.tailIterator(current);
            while (iti.hasNext()) {
                SapProxy<E, T> test = iti.next();
                // dont compare objects against themselves
                if (test.collidable == current.collidable) continue;
                // dont compare object that have already been compared
                if (test.tested) continue;
                // test overlap
                // the >= is to support degenerate intervals created by vertical segments
                if (current.aabb.getMaxX() >= test.aabb.getMinX()) {
                    if (current.aabb.overlaps(test.aabb)) {
                        if (filter.isAllowed(current.collidable, current.fixture, test.collidable, test.fixture)) {
                            pairs.add(new BroadphasePair<>(
                                    current.collidable,
                                    current.fixture,
                                    test.collidable,
                                    test.fixture));
                        }
                    }
                } else {
                    // otherwise we can break from the loop
                    break;
                }
            }
            current.tested = true;
        }

        return pairs;
    }

    @Override
    public List<BroadphaseItem<E, T>> detect(AABB aabb, BroadphaseFilter<E, T> filter) {
        // get the size of the proxy list
        int size = this.tree.size();

        // check the size of the proxy list
        if (size == 0) {
            // return the empty list
            return Collections.emptyList();
        }

        List<BroadphaseItem<E, T>> list = new ArrayList<BroadphaseItem<E, T>>(Collisions.getEstimatedCollisionsPerObject());

        // we must check all aabbs starting at the root
        // from which point the first aabb to not intersect
        // flags us to stop O(n)
        Iterator<SapProxy<E, T>> it = this.tree.inOrderIterator();
        while (it.hasNext()) {
            SapProxy<E, T> proxy = it.next();
            // check for overlap
            if (proxy.aabb.getMaxX() > aabb.getMinX()) {
                if (proxy.aabb.overlaps(aabb)) {
                    if (filter.isAllowed(aabb, proxy.collidable, proxy.fixture)) {
                        list.add(new BroadphaseItem<E, T>(
                                proxy.collidable,
                                proxy.fixture));
                    }
                }
            } else if (aabb.getMaxX() < proxy.aabb.getMinX()) {
                // if not overlapping, then nothing after this
                // node will overlap either so we can exit the loop
                break;
            }
        }

        return list;    }
}
