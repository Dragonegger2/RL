package com.sad.function.input.events;

import com.sad.function.common.Event;
import com.sad.function.input.InputType;

public abstract class InputEvent extends Event {
    private InputType inputType;

    InputEvent(InputType inputType) {
        name = "INPUT_EVENT";
        this.inputType = inputType;
    }

    public InputType getInputType() {
        return inputType;
    }

    public abstract boolean released();
    public abstract boolean pressed();
    public abstract boolean down();
}
