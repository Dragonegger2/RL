package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.sad.function.components.InputHandler;
import com.sad.function.event.Event;
import com.sad.function.event.EventType;
import com.sad.function.event.InputEvent;
import com.sad.function.global.Global;
import com.sad.function.input.context.InputContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InputHandlingSystem extends IteratingSystem {
    private static final Logger logger = LogManager.getLogger(InputHandlingSystem.class);

    private final ComponentMapper<InputHandler> inputHandler = ComponentMapper.getFor(InputHandler.class);

    private List<InputContext> activeContexts;

    public InputHandlingSystem() {
        super(Family.one(InputHandler.class).get());

        activeContexts = new ArrayList<>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
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
                }
            }
        } else {
            logger.warn("No device registered for this handler: {}", inputHandler.getComponentId());
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
}
