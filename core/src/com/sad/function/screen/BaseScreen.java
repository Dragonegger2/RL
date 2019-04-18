package com.sad.function.screen;

import com.sad.function.input.definitions.InputConstants;

public abstract class BaseScreen {
    private static InputConstants.Contexts contextName;

    BaseScreen() {
        contextName = InputConstants.Contexts.TEST;
    }
    abstract void initialize();
    public abstract void enter();
    public abstract void exit();
}