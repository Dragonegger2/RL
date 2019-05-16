package com.sad.function.factory;

import com.sad.function.components.TextureComponent;

import java.util.HashMap;

public class TextureComponentFactory implements ComponentFactory {
    private HashMap<String, TextureComponent> textureComponents;

    public TextureComponentFactory() {
        textureComponents = new HashMap<>();
    }

    public void initialize(String path) {
        if(textureComponents.containsKey(path)) {

        }
    }
}
