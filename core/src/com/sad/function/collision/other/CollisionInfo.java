package com.sad.function.collision.other;

import com.badlogic.gdx.math.Vector2;

public class CollisionInfo {
    public IShape shapeA;
    public IShape shapeB;
    public float distance = 0;              //How much overlap there is.
    public Vector2 pVector;                 // the direction you need to move - unit vector
    public boolean shapeAContained = false; //Is object shapeA contained in object shapeB.
    public boolean shapeBContained = false; //Is object shapeB contained in object shapeA.
    public Vector2 separation;              //Vector that when subtracted from shape A will separate it from ShapeB.
}
