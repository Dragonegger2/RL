package com.sad.function.system.collision;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AABB {
    public float minX,
            minY,
            minZ,
            maxX,
            maxY,
            maxZ,
            surfaceArea;

    public AABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;

        surfaceArea = calculateSurfaceArea();
    }

    private float calculateSurfaceArea() {
        return 2.0f * (getWidth() * getHeight() + getWidth() * getDepth() + getHeight() * getDepth());
    }

    public boolean contains(final AABB other) {
        return other.minX >= minX &&
                other.maxX <= maxX &&
                other.minY >= minY &&
                other.maxY <= maxY &&
                other.minZ >= minZ &&
                other.maxZ <= maxZ;
    }

    public float getWidth() {
        return maxX - minX;
    }

    public float getHeight() {
        return maxY - minY;
    }

    public AABB merge(final AABB other) {
        return new AABB(min(minX, other.minX), min(minY, other.minY), min(minZ, other.minZ),
        max(maxX, other.maxX), max(maxY, other.maxY), max(maxZ, other.maxZ));
    }

    public float getDepth() {
        return maxZ - minZ;
    }
}
