package com.sad.function.global;

import com.badlogic.ashley.core.Entity;
import com.sad.function.input.InputJsonReader;
import com.sad.function.input.definitions.Context;

import java.util.List;

public class Global {
    /**
     * Loaded textures.
     */
    public static Textures textures = new Textures();

    /**
     * Every defined game contexts.
     */
    public static List<Context> definedGameContexts = InputJsonReader.readContextDefinitions();

    /**
     * Currently active game contexts.
     */
    public static Context activeContextsChain;

    public Entity globalEntity = new Entity();
}

