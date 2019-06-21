package com.sad.function.collision.overlay.collision.broadphase;

import com.badlogic.gdx.math.collision.Ray;
import com.sad.function.collision.overlay.collision.AABB;
import com.sad.function.collision.overlay.container.Fixture;

public interface IBroadphaseFilter<E extends Collidable<T>, T extends Fixture> {
    boolean isAllowed(E collidable1, T fixture1, E collidable2, T fixture2);
    boolean isAllowed(AABB aabb, E collidable, T fixture);
    boolean isAllowed(Ray ray, float length, E collidable, T fixture);
}
