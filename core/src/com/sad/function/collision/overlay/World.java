package com.sad.function.collision.overlay;

import com.sad.function.collision.overlay.collision.narrowphase.NarrowPhaseDetector;
import com.sad.function.collision.overlay.container.Fixture;

import java.util.List;

public class World<T extends Fixture> {
    private List<T> bodies;
    private NarrowPhaseDetector narrowPhase;


    public void setNarrowPhaseDetector(NarrowPhaseDetector npd) {
        this.narrowPhase = npd;
    }


    /**
     * Registers a body to be managed by the world.
     * @param body to register.
     * @return if the body was added to the list.
     */
    public boolean addBody(T body) {
        return bodies.add(body);
    }
}
