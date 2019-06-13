package com.sad.function.collision.differ.data;

import com.sad.function.collision.differ.shapes.Shape;

public class ShapeCollision {
    public float overlap = 0.0f;
    public float separationX = 0.0f;
    public float separationY = 0.0f;
    public float unitVectorX = 0.0f;
    public float unitVectorY = 0.0f;

    public float otherOverlap = 0.0f;
    public float otherSeparationX = 0.0f;
    public float otherSeparationY = 0.0f;
    public float otherUnitVectorX = 0.0f;
    public float otherUnitVectorY = 0.0f;

    public Shape shape1;
    public Shape shape2;

    public ShapeCollision reset() {
        shape1 = shape2 = null;
        overlap = separationX = separationY = unitVectorX = unitVectorY = 0.0f;
        otherOverlap = otherSeparationX = otherSeparationY = otherUnitVectorX = otherUnitVectorY = 0.0f;

        return this;
    }

    public ShapeCollision clone() {
        ShapeCollision _clone = new ShapeCollision();

        _clone.copy(this);

        return _clone;
    }

    public void copy(ShapeCollision _other) {

        overlap = _other.overlap;
        separationX = _other.separationX;
        separationY = _other.separationY;
        unitVectorX = _other.unitVectorX;
        unitVectorY = _other.unitVectorY;
        otherOverlap = _other.otherOverlap;
        otherSeparationX = _other.otherSeparationX;
        otherSeparationY = _other.otherSeparationY;
        otherUnitVectorX = _other.otherUnitVectorX;
        otherUnitVectorY = _other.otherUnitVectorY;
        shape1 = _other.shape1;
        shape2 = _other.shape2;
    }
}
