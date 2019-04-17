package com.sad.function.input;

import com.badlogic.ashley.core.Entity;
import com.sad.function.common.Observer;
import com.sad.function.common.Subject;
import com.sad.function.event.Event;

/**
 * Holds the active contexts and dispatches events to them from the InputStateManager.
 *
 * I'm curious if this is actually needed.
 * TODO: Figure above out.
 */
public class ContextManager extends Subject implements Observer {
    public void onNotify(Entity entity, Event event) {
        if (event.getType() == Event.EventType.INPUT) {
            //Send the message on to the context list.
            getObservers().forEach(observer -> observer.onNotify(null, event));
        }
    }
}
