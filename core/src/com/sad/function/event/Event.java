package com.sad.function.event;

public class Event {
    protected EventType type;

    public Event(EventType type) { this.type = type; }
    public EventType getEventType() { return type; }
}
