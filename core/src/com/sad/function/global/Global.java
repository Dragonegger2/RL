package com.sad.function.global;

import com.sad.function.input.BindingsLinker;
import com.sad.function.input.InputContext;
import com.sad.function.input.definitions.InputConstants;

import java.util.HashMap;

public class Global {
    public static Textures textures = new Textures();

    public static HashMap<InputConstants.Contexts, InputContext> gameContexts = BindingsLinker.readBindings();
}
