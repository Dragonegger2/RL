package com.sad.function.collision.overlay.broadphase;

import com.sad.function.collision.overlay.Collidable;
import com.sad.function.collision.overlay.data.AABB;
import com.sad.function.collision.overlay.container.Fixture;


public class SapProxy<E extends Collidable<T>, T extends Fixture> implements Comparable<SapProxy<E, T>> {
    final E collidable;
    final T fixture;

    AABB aabb;

    boolean tested;

    public SapProxy(E collidable, T fixture, AABB aabb) {
        this.collidable = collidable;
        this.fixture = fixture;
        this.aabb = aabb;
    }

    @Override
    public int compareTo(SapProxy<E, T> o) {
        // check if the objects are the same instance
        if (this == o) return 0;
        // compute the difference in the minimum x values of the aabbs
        double diff = this.aabb.getMinX() - o.aabb.getMinX();
        if (diff != 0) {
            return (int)Math.signum(diff);
        } else {
            // if the x values are the same then compare on the y values
            diff = this.aabb.getMinY() - o.aabb.getMinY();
            if (diff != 0) {
                return (int)Math.signum(diff);
            } else {
                // finally if their y values are the same then compare on the ids
                diff = this.collidable.getId().compareTo(o.collidable.getId());
                if (diff == 0) {
                    return this.fixture.getId().compareTo(o.fixture.getId());
                }
                return (int)Math.signum(diff);
            }
        }
    }
}
