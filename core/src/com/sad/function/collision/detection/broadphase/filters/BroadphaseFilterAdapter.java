package com.sad.function.collision.detection.broadphase.filters;

import com.badlogic.gdx.math.collision.Ray;
import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.AABB;

/**
 * Returns true for all instances of collidables.
 */
public class BroadphaseFilterAdapter implements BroadphaseFilter{

    @Override
    public boolean isAllowed(Body collidable1, Fixture fixture1, Body collidable2, Fixture fixture2) {
        return true;
    }

    @Override
    public boolean isAllowed(AABB aabb, Body collidable, Fixture fixture) {
        return true;
    }

    @Override
    public boolean isAllowed(Ray ray, float length, Body collidable, Fixture fixture) {
        return true;
    }
}
