package com.sad.function.components;

import com.badlogic.ashley.core.Component;

/**
 * Holds a reference to the internal path to use for a texture.
 */
public class TextureComponent implements Component {
    //TODO Make use of a flyweight pattern here. We'll have a ton of objects that are similar.
    public String internalPath;
    public int width;
    public int height;

    /**
     * Default constructor means default images.
     */
    public TextureComponent() {
        this("null");

        width = 256;
        height = 256;
    }

    public TextureComponent(String path) {
        internalPath = path;

        width = 32;
        height = 32;
    }

    public TextureComponent setHeight(int height) {
        this.height = height;
        return this;
    }

    public TextureComponent setWidth(int width) {
        this.width = width;
        return this;
    }
}
