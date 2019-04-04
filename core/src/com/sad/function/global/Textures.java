package com.sad.function.global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class Textures {
    /**
     * Internal file location, object.
     */
    private HashMap<String, Texture> textures = new HashMap<>();

    /**
     * Fetch a texture.
     * @param internalName
     * @return
     */
    public Texture get(String internalName) {
        if(textures.get(internalName) == null) {
            //Try loading resource.
            textures.put(internalName, new Texture(Gdx.files.internal(internalName)));
        }

        return textures.get(internalName);
    }

    /**
     * Drops all loaded textures from memory.
     */
    public void dispose() {
        textures.values().forEach(Texture::dispose);
    }
}
