package com.sad.function.collision.overlay.narrowphase;

import com.sad.function.collision.overlay.broadphase.BroadphasePair;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.data.Penetration;

import java.util.ArrayList;
import java.util.List;

public class NarrowPhase {
    //TODO: Maybe a listener system to the parent system for this.
    private NarrowPhaseDetector narrowPhase = new GJK();

    public List<CollisionManifold> solve(List<BroadphasePair<Body, BodyFixture>> potentialPairs) {
        List<CollisionManifold> manifolds = new ArrayList<>(potentialPairs.size());//Presize to all of the potentialpairs.

        Penetration p = new Penetration();
        for(BroadphasePair<Body, BodyFixture> pair : potentialPairs) {
            if(narrowPhase.detect(pair.getFixture1().getShape(), pair.getCollidable1().getTransform(),
                    pair.getFixture2().getShape(), pair.getCollidable2().getTransform(), p)) {
                //Add it to the results.
                manifolds.add(new CollisionManifold(p.normal.cpy(), p.distance, pair.getCollidable1(), pair.getFixture1(), pair.getCollidable2(), pair.getFixture2()));
            }
        }

        return manifolds;
    }

}
