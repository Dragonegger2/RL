package com.sad.function.system;

import com.badlogic.gdx.math.Rectangle;

/**
 * Narrow spacial manager. Collision detection using Minkowski Difference.
 */
class NarrowSpacialManager {
    /**
     * Returns the Minkowski Difference rectangle if the objects are colliding, otherwise it returns null.
     *
     * Calculates using Minkowski Difference.
     * @param aX            object a's x position
     * @param aY            object a's y position
     * @param aWidth        object a's width
     * @param aHeight       object a's height
     * @param bX            object b's x position
     * @param bY            object b's y position
     * @param bWidth        object b's width
     * @param bHeight       object b's height
     * @return whether the objects provided are colliding.
     */
    Rectangle minkowskiDifference(float aX, float aY, float aWidth, float aHeight, float bX, float bY, float bWidth, float bHeight) {
        Rectangle md = new Rectangle(
                aX - (bX + bWidth),
                aY - (bY + bHeight), //Calculate the bottom, not the top like in the example AABBMD_TOP = AABBA_TOP - AABB_B_BOTTOM
                aWidth + bWidth,
                aHeight + bHeight
        );

        return md;
    }

//    public boolean areCollidint(float aX, float aY, float aWidth, float aHeight, float bX, float bY, float bWidth, float bHeight, Vector2 penetration) {
//        Rectangle minkowski;
//
//    }
}