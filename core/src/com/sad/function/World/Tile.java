package com.sad.function.World;

import com.badlogic.ashley.core.Entity;

/**
 * Represents all the given data that a single world tile can have.
 */
public class Tile {
    private int value;
    private int x;
    private int y;

    public Tile(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public Entity getTileEntity() {
        return new Entity();
    }
}