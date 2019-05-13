package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Input;
import com.sad.function.components.InputHandler;
import com.sad.function.event.Event;
import com.sad.function.event.EventType;
import com.sad.function.event.input.InputEvent;
import com.sad.function.global.Global;
import com.sad.function.input.context.InputContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InputHandlingSystem implements EntityListener {
    private static final Logger logger = LogManager.getLogger(InputHandlingSystem.class);

    private final ComponentMapper<InputHandler> inputHandler = ComponentMapper.getFor(InputHandler.class);

    private List<InputContext> activeContexts;
    private List<Entity> entitiesWithInputHandlers;

    public InputHandlingSystem() {
        activeContexts = new ArrayList<>();
        entitiesWithInputHandlers = new ArrayList<>();
    }

    private void processEntity(Entity entity, float deltaTime) {
        InputHandler inputHandler = this.inputHandler.get(entity);

        //Make sure it has a handler registered.
        if (inputHandler.getDeviceId() != null) {
            List<Event> eventList = Global.deviceManager.pollDevice(inputHandler.getDeviceId());

            for (Event event : eventList) {
                if (event.getEventType() == EventType.INPUT) {
                    //Do things.
                    InputEvent inputEvent = (InputEvent) event;

                    String actionName = findInContext(Input.Keys.toString(inputEvent.getId()));

                    //There's a corresponding action for this input.
                    if (actionName != null) {
                        //Dispatch the action and event.
                        inputHandler.handleAction(actionName, entity, inputEvent, deltaTime);
                    }

                    //Release is back into memory.
                    inputEvent.inUse = false;
                }

            }
        } else {
            logger.warn("No device registered for this handler: {}", inputHandler.getComponentId());
        }
    }

    public void handleInput(float delta) {
        for(Entity entity : entitiesWithInputHandlers) {
            processEntity(entity, delta);
        }
    }

    public void pushContext(InputContext context) {
        activeContexts.add(context);
        logger.info("Context added: {}", context.getName());
    }

    private String findInContext(String target) {
        for (InputContext activeContext : activeContexts) {
            String value = activeContext.findMappedInput(target);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    @Override
    public void entityAdded(Entity entity) {
        logger.info("Added new entity with input handler {}.", entity);
        entitiesWithInputHandlers.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        logger.info("Removed entity with input handler {}.", entity);
        entitiesWithInputHandlers.remove(entity);
    }
}
