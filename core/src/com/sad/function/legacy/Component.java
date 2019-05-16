package com.sad.function.legacy;

import java.util.UUID;

/**
 * The base component class. Adding an ID for uniqueness and debugging purposes.
 */
public abstract class Component {

    private UUID ID;

    public Component() {
        ID = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return ID + ": " + this.getClass().getName();
    }
}