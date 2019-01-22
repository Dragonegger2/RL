package com.sad.function.screen;

import com.badlogic.ashley.core.Engine;
import com.sad.function.global.Global;
import com.sad.function.input.definitions.InputContext;
import com.sad.function.input.definitions.InputConstants;

public abstract class BaseScreen {
    private static Engine engine = new Engine();
    private static InputConstants.Contexts contextName;

    BaseScreen() {
        contextName = InputConstants.Contexts.TEST;
    }
    abstract void initialize();
    public abstract void enter();
    public abstract void exit();

    public Engine engine() { return engine; }

    public InputContext InputContextActions() {
        return Global.gameContexts.get(contextName);
    }
}