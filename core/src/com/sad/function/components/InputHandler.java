package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.sad.function.command.GameCommand;
import com.sad.function.input.states.InputActionType;
import com.sad.function.event.input.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.UUID;

public class InputHandler extends com.artemis.Component implements Component {
    private UUID deviceId;
    private UUID componentId;

    private static Logger logger = LogManager.getLogger(InputHandler.class);

    private LinkedHashSet<String> actionNames;
    private ArrayList<GameCommand> commandList;
    private ArrayList<InputActionType> actionTypeList;

    public InputHandler() {
        actionNames = new LinkedHashSet<>();
        commandList = new ArrayList<>();
        actionTypeList = new ArrayList<>();

        componentId = UUID.randomUUID();
    }

    /**
     * Handles a mid-level input event.
     *
     * @param actionName type of the action, such as "FIRE"
     * @param entity     that components will act upon.
     * @param deltaTime  in time since last time rendered. Some commands require this.
     * @return true if handled, false if not.
     */
    public boolean handleAction(String actionName, Entity entity, InputEvent event, float deltaTime) {
        if (actionNames.contains(actionName)) {
            //Find index of actionName:
            int index = getIndex(actionName);

            //Matching action type.
            if (matchesValue(actionTypeList.get(index), event)) {
                commandList.get(index).execute(entity, deltaTime);
                return true;
            }
        }

        return false;
    }

    /**
     * Registers a new game action with a given type.
     *
     * @param name   of the new action to register to this input handler.
     * @param action to register with this input handler.
     */
    public void associateAction(String name, InputActionType inputActionType, GameCommand action) {
        if (actionNames.add(name)) {
            logger.info("Binding a new action with an input handler Name:{} InputActionType: {} GameCommand: {}", name, inputActionType, action);
            actionTypeList.add(inputActionType);
            commandList.add(action);
        } else {
            logger.error("Tried to register additional action with duplicate type: {}", name);
        }
    }

    private int getIndex(String target) {
        int index = 0;
        Iterator<String> actionNameIterator = actionNames.iterator();
        while (actionNameIterator.hasNext()) {
            String name = actionNameIterator.next();
            if (name.equals(target)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private boolean matchesValue(InputActionType inputActionType, InputEvent event) {
        if(inputActionType == InputActionType.ON_PRESS_ONLY || inputActionType == InputActionType.REPEAT_WHILE_DOWN) {
            if(event.getValue() == 1) {
                return true;
            }
        }

        return inputActionType == InputActionType.ON_PRESS_AND_RELEASE && event.getValue() == 0;
    }

    public UUID getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public UUID getComponentId() { return componentId; }
}
