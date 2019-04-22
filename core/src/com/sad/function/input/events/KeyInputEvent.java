package com.sad.function.input.events;


public class KeyInputEvent extends InputEvent {
    private float value;

    public KeyInputEvent() {
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
