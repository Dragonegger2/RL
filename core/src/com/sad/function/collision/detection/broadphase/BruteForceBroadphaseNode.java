package com.sad.function.collision.detection.broadphase;

import com.sad.function.collision.data.AABB;
import com.sad.function.collision.overlay.container.Fixture;
import com.sad.function.collision.data.Transform;

public class BruteForceBroadphaseNode<E extends Collidable<T>, T extends Fixture> {
    public final E collidable;
    public final T fixture;
    public AABB aabb;
    boolean tested;

    BruteForceBroadphaseNode(E collidable, T fixture) {
        this.collidable = collidable;
        this.fixture = fixture;

        this.updateAABB();
    }

    void updateAABB() {
        Transform tx = collidable.getTransform();
        this.aabb = fixture.getShape().createAABB(tx);
    }
}
