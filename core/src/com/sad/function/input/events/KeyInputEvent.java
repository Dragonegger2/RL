package com.sad.function.input.events;

import com.sad.function.input.InputType;

public class KeyInputEvent extends InputEvent {
    private float value;
    private int id;

    public KeyInputEvent() {
        super(InputType.KEYBOARD);
        name = "KEYBOARD_INPUT_EVENT";
    }

    public KeyInputEvent setValue(float value) {
        this.value = value;
        return this;
    }

    public KeyInputEvent setId(int id) {
        this.id = id;
        return this;
    }
    /*
     1 PRESSED
     0 RELEASED
    -1 DOWN
     */
    public float getValue() { return value; }

    public int getId() {
        return id;
    }

    @Override
    public boolean released() {
        return value == 0;
    }

    @Override
    public boolean pressed() {
        return value == 1;
    }

    @Override
    public boolean down() {
        return value == -1;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof KeyInputEvent) {
            KeyInputEvent passedObj = (KeyInputEvent)obj;
            return this.id == passedObj.id && this.value == passedObj.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (id * name.hashCode() * value);
    }
}
