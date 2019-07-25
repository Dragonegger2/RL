package com.sad.function.collision.data;

/**
 * Result of a TOI calculation.
 * <p>
 * Results in the exact time of impact, between 0 and 1. 0%-100%. This multiplied by the delta in this
 * time step we know how long this must have taken.
 *
 * This object is meant to be reused.
 */
public class TimeOfImpact {
    protected float time;
    protected Separation separation;

    public TimeOfImpact() {}
    public TimeOfImpact(float time, Separation separation) {
        this.time = time;
        this.separation = separation;
    }

    public float getTime() {
        return this.time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public Separation getSeparation() {
        return this.separation;
    }

    public void setSeparation(Separation separation) {
        this.separation = separation;
    }
}
