package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.sad.function.global.Global;

/**
 * Holds a reference to the internal path to use for a texture.
 */
public class TextureComponent implements Component {

    public Texture texture;
    public String internalPath;
    public int width;
    public int height;

    /**
     * Default constructor means default images.
     */
    public TextureComponent() {
        internalPath = "badlogic.jpg";
        texture = Global.textures.get(internalPath);
        width = 256;
        height = 256;
    }

    public TextureComponent(String path) {
        internalPath = path;
        texture = Global.textures.get(internalPath);
        height = texture.getHeight();
        width = texture.getWidth();
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
