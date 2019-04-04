package com.sad.function.components;

import com.badlogic.ashley.core.Component;

/**
 * Holds a reference to the internal path to use for a texture.
 */
public class Texture implements Component {

    public String internalPath;

    /**
     * Default constructor means default images.
     */
    public Texture() {
        internalPath = "badlogic.jpg";
    }
}
