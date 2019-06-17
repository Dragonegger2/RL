package com.sad.function.collision.overlay;

public class Projection {
    private float min;
    private float max;

    public Projection(float min, float max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns if two projections are overlapping.
     * @param p2 projection compare to
     * @return if the two projects overlap.
     */
    public boolean overlaps(Projection p2) {
        return !(this.min > p2.max || p2.min > this.max);
    }

    /**
     * Returns the overlap value of the two projections
     * @param p2 to compare with.
     * @return overlap value OR 0 if they do not overlap.
     */
    public float getOverlap(Projection p2) {
        if(this.overlaps(p2)) {
            return Math.min(this.max, p2.max) - Math.max(this.min, p2.min);
        }
        return 0;
    }

    public boolean contains(Projection p2) {
        return p2.min > this.min && p2.max < this.max;
    }

    public float getMin() { return this.min; }
    public float getMax() { return this.max; }
}
