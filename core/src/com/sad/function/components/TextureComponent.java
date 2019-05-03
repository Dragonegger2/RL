package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.sad.function.global.Global;

/**
 * Holds a reference to the internal path to use for a texture.
 */
public class TextureComponent implements Component {

    public String internalPath;

    /**
     * Default constructor means default images.
     */
    public TextureComponent() {
        internalPath = "badlogic.jpg";
    }

    public TextureComponent(String path) {
        internalPath = path;
    }

    public Texture getTexture() {
        return Global.textures.get(internalPath);
    }
}
