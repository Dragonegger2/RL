package com.sad.function.collision.overlay.collision.narrowphase;

import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.ArrayList;
import java.util.List;

public abstract class NarrowPhase<T extends Fixture> {
    protected List<T> bodies;

    protected NarrowPhase() {
        bodies = new ArrayList<>();
    }

    /**
     * Register this body with the narrow phase.
     * @param fixture to register
     * @return whether the body was registered with this.
     */
    public boolean addBody(T fixture) {
        return this.bodies.add(fixture);
    }

    /**
     * Tries to remove this body from the narrow phase.
     * @param fixture to remove.
     * @return whether the body was unregistered with this detector.
     */
    public boolean removeBody(T fixture) {
        return this.bodies.remove(fixture);
    }
}
