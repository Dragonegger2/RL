package com.sad.function.input.events;

import com.sad.function.common.Event;

public abstract class InputEvent extends Event {
    int id;
    float value;

    public int getId() { return id; }
    public float getValue() { return value; }


}
