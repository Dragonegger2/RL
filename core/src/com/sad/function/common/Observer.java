package com.sad.function.common;

import com.badlogic.ashley.core.Entity;
import com.sad.function.event.Event;

public abstract class Observer {
    /**
     * Notify this observer of a subject that they are registered to listen for. They will do their own message parsing.
     * @param entity
     * @param event
     */
    public abstract void onNotify(Entity entity, Event event);
}
