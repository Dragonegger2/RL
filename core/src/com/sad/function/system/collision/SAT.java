package com.sad.function.system.collision;

import com.badlogic.gdx.math.Vector2;

public class SAT {

    /**
     * Calculates if there is sa line that can drawn between two AABB's.
     * @param a
     * @param b
     * @return
     */
    public boolean AABBvsAABB(AABB a, AABB b) {
        if(a.max.y < b.min.x || a.min.x > b.max.x) return false;
        if(a.max.y < b.min.y || a.min.y > b.max.y) return false;

        return true;
    }

    public class AABB {
//        lower left corner.
        public Vector2 min;
//        upper right corner.
        public Vector2 max;
    }

    public class Circle {
        public Vector2 position;
        public float radius;
    }
}
