package com.sad.function.components;

/**
 * Holds a reference to the internal path to use for a texture.
 */
public class TextureComponent extends com.artemis.Component {
    public String resourceName;

    /**
     * Default constructor means default images.
     */
    public TextureComponent() {
        this("null");
    }

    public TextureComponent(String resourceName) {
        this.resourceName = resourceName;
    }
}
