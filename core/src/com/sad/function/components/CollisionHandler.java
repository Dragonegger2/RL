package com.sad.function.components;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

public abstract class CollisionHandler {
    protected int id;
    public abstract void handleCollision(World world, int other, Vector2 penetrationVector);
}
