package com.sad.function.collision.overlay;

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

    public static AABB createAABBFromPoints(Vector2 point1, Vector2 point2) {
        return createAABBFromPoints(point1.x, point1.y, point2.x, point2.y);
    }

    private static AABB createAABBFromPoints(float point1x, float point1y, float point2x, float point2y) {
        if (point2x < point1x) {
            float temp = point1x;
            point1x = point2x;
            point2x = temp;
        }
        if (point2y < point1y) {
            float temp = point2y;
            point1y = point2y;
            point2y = temp;
        }

        return new AABB(point1x, point1y, point2x, point2y);
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

    public AABB expand(float expansion) {
        float e = expansion * 0.5f;
        this.minX -= e;
        this.minY -= e;
        this.maxX += e;
        this.maxY += e;

        if (expansion < 0.0) {
            if (this.minX > this.maxX) {
                float mid = (this.minX + this.maxX) * 0.5f;
                this.minX = mid;
                this.maxX = mid;
            }
            if (this.minY > this.maxY) {
                float mid = (this.minY + this.maxY) * 0.5f;
                this.minY = mid;
                this.maxY = mid;
            }
        }
        return this;
    }
}
