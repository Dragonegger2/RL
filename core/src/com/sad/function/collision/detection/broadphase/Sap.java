package com.sad.function.collision.detection.broadphase;

import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.AABB;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.detection.broadphase.filters.BroadphaseFilter;
import org.dyn4j.BinarySearchTree;
import org.dyn4j.collision.Collisions;

import java.util.*;

import static com.sad.function.collision.detection.broadphase.AbstractBroadphaseDetector.createKey;

public class Sap {//extends AbstractBroadphaseDetector {//implements BroadphaseDetector {
    BinarySearchTree<SapProxy> tree;
    Map<Integer, SapProxy> map;
    float expansion = 0;

    public Sap() {
        this(64);
    }

    public Sap(int initialSize) {
        this.tree = new BinarySearchTree<>(true);
        map = new HashMap<>(initialSize * 4 / 3 + 1, 0.75f);
    }

    public void add(Body collidable, Fixture fixture, Transform transform) {
        int key = createKey(collidable, fixture);
        SapProxy proxy = this.map.get(key);
        if (proxy == null) {
            this.add(key, collidable, transform, fixture);
        } else {
            this.update(key, proxy, collidable, transform, fixture);
        }
    }

    private void add(int key, Body collidable, Transform tx, Fixture fixture) {
        AABB aabb = fixture.getShape().createAABB(tx);
        // expand the aabb
        aabb.expand(this.expansion);
        // create a new node for the collidable
        SapProxy proxy = new SapProxy(collidable, fixture, aabb);
        // add the proxy to the map
        this.map.put(key, proxy);
        // insert the node into the tree
        this.tree.insert(proxy);
    }

    public boolean remove(Body collidable, Fixture fixture) {
        int key = createKey(collidable, fixture);
        SapProxy proxy = map.remove(key);
        if (proxy != null) {
            tree.remove(proxy);
            return true;
        }

        return false;
    }

    public void update(Body collidable, Transform transform, Fixture fixture) {
        int key = createKey(collidable, fixture);
        SapProxy proxy = this.map.get(key);
        if (proxy != null) {
            this.update(key, proxy, collidable, transform, fixture);
        } else {
            this.add(key, collidable, transform, fixture);
        }
    }

    private void update(int key, SapProxy proxy, Body collidable, Transform tx, Fixture fixture) {
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

    public AABB getAABB(Body collidable, Transform transform, Fixture fixture) {
        int key = createKey(collidable, fixture);
        SapProxy proxy = map.get(key);
        if (proxy != null) {
            return proxy.aabb;
        }
        return fixture.getShape().createAABB(transform);
    }

    public boolean contains(Body collidable, Fixture fixture) {
        int key = createKey(collidable, fixture);
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
        tree.clear();
    }

    public List<BroadphasePair> detect(BroadphaseFilter filter) {
        // get the number of proxies
        int size = this.tree.size();

        // check the size
        if (size == 0) {
            // return the empty list
            return Collections.emptyList();
        }

        // the estimated size of the pair list

        int eSize = Collisions.getEstimatedCollisionPairs(size); //TODO: Remove this dependency at some point.
        List<BroadphasePair> pairs = new ArrayList<>(eSize);

        // clear the tested flags
        Iterator<SapProxy> itp = this.tree.iterator();
        while (itp.hasNext()) {
            SapProxy p = itp.next();
            p.tested = false;
        }

        // find all the possible pairs O(n*log(n))
        Iterator<SapProxy> ito = this.tree.iterator();
        while (ito.hasNext()) {
            // get the current proxy
            SapProxy current = ito.next();
            Iterator<SapProxy> iti = this.tree.tailIterator(current);
            while (iti.hasNext()) {
                SapProxy test = iti.next();
                // dont compare objects against themselves
                if (test.collidable == current.collidable) continue;
                // dont compare object that have already been compared
                if (test.tested) continue;
                // test overlap
                // the >= is to support degenerate intervals created by vertical segments
                if (current.aabb.getMaxX() >= test.aabb.getMinX()) {
                    if (current.aabb.overlaps(test.aabb)) {
                        if (filter.isAllowed(current.collidable, current.fixture, test.collidable, test.fixture)) {
                            pairs.add(new BroadphasePair(
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

    public List<BroadphaseItem> detect(AABB aabb, BroadphaseFilter filter) {
        // get the size of the proxy list
        int size = this.tree.size();

        // check the size of the proxy list
        if (size == 0) {
            // return the empty list
            return Collections.emptyList();
        }

        List<BroadphaseItem> list = new ArrayList<BroadphaseItem>(Collisions.getEstimatedCollisionsPerObject());

        // we must check all aabbs starting at the root
        // from which point the first aabb to not intersect
        // flags us to stop O(n)
        Iterator<SapProxy> it = this.tree.inOrderIterator();
        while (it.hasNext()) {
            SapProxy proxy = it.next();
            // check for overlap
            if (proxy.aabb.getMaxX() > aabb.getMinX()) {
                if (proxy.aabb.overlaps(aabb)) {
                    if (filter.isAllowed(aabb, proxy.collidable, proxy.fixture)) {
                        list.add(new BroadphaseItem(
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

        return list;
    }
}
