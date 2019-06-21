package com.sad.function.collision.overlay.collision;

import com.badlogic.gdx.math.Vector2;

public class AABB implements Translateable {
    float minX, minY;
    float maxX, maxY;

    public AABB(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * @param aabb
     */
    public AABB(AABB aabb) {
        this.minX = aabb.minX;
        this.maxX = aabb.maxX;
        this.minY = aabb.minY;
        this.maxY = aabb.maxY;
    }

    /**
     * Full constructor.
     *
     * @param center
     * @param radius
     */
    public AABB(Vector2 center, float radius) {
        if (center == null) {
            this.minX = -radius;
            this.minY = -radius;
            this.maxX = radius;
            this.maxY = radius;
        } else {
            this.minX = center.x - radius;
            this.minY = center.y - radius;
            this.maxX = center.x + radius;
            this.maxY = center.y + radius;
        }
    }

    public AABB set(AABB aabb) {
        this.minX = aabb.minX;
        this.maxX = aabb.maxX;
        this.minY = aabb.minY;
        this.maxY = aabb.maxY;

        return this;
    }

    /**
     * Calculates the width of this {@link AABB}.
     *
     * @return the {@link AABB}'s width.
     */
    public float getWidth() {
        return this.maxX - this.minX;
    }

    /**
     * Calculates the height of this AABB.
     *
     * @return the AABB's height.
     */
    public float getHeight() {
        return this.maxY - this.maxY;
    }

    @Override
    public void translate(float x, float y) {
        this.minX += x;
        this.maxX += x;
        this.minY += y;
        this.maxY += y;
    }

    @Override
    public void translate(Vector2 translation) {
        this.translate(translation.x, translation.y);
    }

    /**
     * Stretches this {@link AABB}
     *
     * @param aabb to union this {@link AABB} with.
     * @return {@link AABB}
     */
    public AABB union(AABB aabb) {
        this.minX = Math.min(minX, aabb.minX);
        this.minY = Math.min(minY, aabb.minY);
        this.maxX = Math.max(maxX, aabb.maxX);
        this.maxY = Math.max(maxY, aabb.maxY);
        return this;
    }

    public boolean overlaps(AABB aabb) {
        return this.minX <= aabb.maxX &&
                this.maxX >= aabb.minX &&
                this.minY <= aabb.maxY &&
                this.maxY >= aabb.minY;
    }

    public boolean contains(AABB aabb) {
        return this.minX <= aabb.minX &&
                this.maxX >= aabb.maxX &&
                this.minY <= aabb.minY &&
                this.maxY >= aabb.maxY;
    }

    public boolean contains(Vector2 point) {
        return contains(point.x, point.y);
    }

    public boolean contains(float x, float y) {
        return this.minX <= x &&
                this.maxX >= x &&
                this.minY <= y &&
                this.maxY >= y;
    }
}
