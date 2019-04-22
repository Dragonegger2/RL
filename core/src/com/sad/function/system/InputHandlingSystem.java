package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sad.function.common.Observer;
import com.sad.function.components.InputHandler;
import com.sad.function.input.events.InputEvent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class InputHandlingSystem extends IteratingSystem implements Observer<List<InputEvent>> {
    private final ComponentMapper<InputHandler> inputHandler = ComponentMapper.getFor(InputHandler.class);

    private Queue<InputEvent> inputInputEventQueue = new LinkedList<>();

    public InputHandlingSystem() {
        super(Family.one(InputHandler.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputHandler handle =this.inputHandler.get(entity);

        Iterator<InputEvent> eventIterator = inputInputEventQueue.iterator();

        while(eventIterator.hasNext()) {
            InputEvent inputEvent = eventIterator.next();

            //TODO: Call context manager/contexts are a part of this system? Makes sense.
        }
    }

    @Override
    public void onNotify(List<InputEvent> inputEvent) {
        inputInputEventQueue.addAll(inputEvent);
    }

    /**
     * Clear the event queue at the end of a tick. We don't want the queue to grow with events
     * that aren't registered.
     */
    public void clearEventQueue() {
        //TODO: Have this inherit from Subject<Event>, dispatch clear events to all registered input devices.
        inputInputEventQueue.clear();
    }
}
