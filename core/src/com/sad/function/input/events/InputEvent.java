package com.sad.function.input.events;

import com.sad.function.common.Event;

public abstract class InputEvent extends Event {
    int id;

    public int getId() { return id; }
}
