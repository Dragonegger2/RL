package com.sad.function.global;

import com.sad.function.common.SizedStack;
import com.sad.function.input.InputJsonReader;
import com.sad.function.input.definitions.InputContext;
import com.sad.function.input.definitions.InputConstants;
import com.sad.function.input.definitions.Keyboard;

import java.util.HashMap;

public class Global {
    public static Textures textures = new Textures();

    public static HashMap<InputConstants.Contexts, InputContext> gameContexts = InputJsonReader.readBindings();

    public static SizedStack<Keyboard> keyboardStates = new SizedStack<>(10);
}
