package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.sad.function.global.Global;

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
        this("badlogic.jpg");

        width = 256;
        height = 256;
    }

    public TextureComponent(String path) {
        internalPath = path;
        if(!Global.assetManager.isLoaded(internalPath)) {
            Global.assetManager.load(internalPath, Texture.class);
            Global.assetManager.finishLoadingAsset(internalPath);
        }

        height = Global.assetManager.get(path, Texture.class).getHeight();
        width = Global.assetManager.get(path, Texture.class).getWidth();
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
