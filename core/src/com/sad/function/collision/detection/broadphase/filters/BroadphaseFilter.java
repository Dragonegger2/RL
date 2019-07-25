package com.sad.function.collision.detection.broadphase.filters;

import com.badlogic.gdx.math.collision.Ray;
import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.AABB;

public interface BroadphaseFilter {
    boolean isAllowed(Body collidable1, Fixture fixture1, Body collidable2, Fixture fixture2);
    boolean isAllowed(AABB aabb, Body collidable, Fixture fixture);
    boolean isAllowed(Ray ray, float length, Body collidable, Fixture fixture);
}
