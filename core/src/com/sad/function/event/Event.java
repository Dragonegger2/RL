package com.sad.function.event;

public class Event {
    private EventType type;

    public Event() {}
    Event(EventType type) {
        this.type = type;
    }

    /**
     * Returns the target event by casting it to a specific event type.
     * @param type of event.
     * @param <E> class to cast this object to.
     * @return class
     */
    public <E extends Event> E getEvent(Class<E> type) {
        return type.cast(this);
    }

    public EventType getType() {
        return type;
    }

    public enum EventType {
        INPUT
    }
}
