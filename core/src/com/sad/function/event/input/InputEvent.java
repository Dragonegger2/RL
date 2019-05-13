package com.sad.function.event.input;

import com.sad.function.event.Event;
import com.sad.function.event.EventType;

public class InputEvent extends Event {
    //TODO Switch around inputs and such.
    int id;
    float value;
    public boolean inUse = false;

    public InputEvent() {
        super(EventType.INPUT);
    }

    public int getId() { return id; }
    public float getValue() { return value; }
    public void initialize(int id, float value) {
        this.id = id;
        this.value = value;
    }
}
