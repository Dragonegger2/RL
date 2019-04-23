package com.sad.function.event;

public abstract class InputEvent extends Event {
    int id;
    float value;

    public InputEvent() {
        super(EventType.INPUT);
    }

    public int getId() { return id; }
    public float getValue() { return value; }
}
