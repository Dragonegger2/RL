package com.sad.function.collision.detection.narrowphase;

import com.sad.function.collision.data.Penetration;
import com.sad.function.collision.geometry.Raycaster;
import com.sad.function.collision.shape.Convex;
import com.sad.function.collision.shape.Shape;
import com.sad.function.collision.data.Transform;

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