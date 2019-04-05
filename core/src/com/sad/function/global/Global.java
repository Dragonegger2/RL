package com.sad.function.global;

import com.sad.function.common.SizedStack;
import com.sad.function.input.InputJsonReader;
import com.sad.function.input.definitions.Context;
import com.sad.function.input.definitions.Keyboard;

import java.util.ArrayList;
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
public static List<Context> activeContexts = new ArrayList<>();

    public static SizedStack<Keyboard> keyboardStates = new SizedStack<>(10);
}
