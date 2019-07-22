package com.sad.function.collision.overlay;

import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.geometry.Raycast;
import com.sad.function.collision.overlay.geometry.Raycaster;
import com.sad.function.collision.overlay.shape.Convex;
import com.sad.function.collision.overlay.shape.Shape;
import com.sad.function.collision.overlay.data.Transform;

public interface NarrowPhaseDetector extends Raycaster {
    /**
     * Returns true if the two provided convex shapes overlap. Fills penetration {@link Penetration} with data.
     * @param convex1       shape that is the basis of comparison.
     * @param transform1    {@link Transform}'s this {@link Shape} by
     * @param convex2
     * @param transform2
     * @param penetration
     * @return
     */
    boolean detect(Convex convex1, Transform transform1, Convex convex2, Transform transform2, Penetration penetration);

    /**
     * Returns true if the two provided convex shapes overlap.
     * @param convex1
     * @param transform1
     * @param convex2
     * @param transform2
     * @return
     */
    boolean detect(Convex convex1, Transform transform1, Convex convex2, Transform transform2);


}