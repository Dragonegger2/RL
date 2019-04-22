package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.sad.function.command.GameCommand;
import com.sad.function.input.InputActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class InputHandler implements Component {
    Logger logger = LogManager.getLogger(InputHandler.class);

    private LinkedHashSet<String> actionNames;
    private ArrayList<GameCommand> commandList;
    private ArrayList<InputActionType> actionTypeList;

    public InputHandler() {
        actionNames = new LinkedHashSet<>();
        commandList = new ArrayList<>();
        actionTypeList = new ArrayList<>();
    }

    /**
     * Handles a mid-level input event.
     * @param actionName name of the action, such as "FIRE"
     * @param entity that components will act upon.
     * @param deltaTime in time since last time rendered. Some commands require this.
     * @return true if handled, false if not.
     */
    public boolean handleAction(String actionName, Entity entity, float deltaTime) {
        if(actionNames.contains(actionName)){
            //Find index of actionName:
            Iterator<String> actionNameIterator = actionNames.iterator();

            int index = 0;
            while (actionNameIterator.hasNext()) {
                String name = actionNameIterator.next();
                if(name.equals(actionName)) {
                    break;
                }
                index++;
            }

            //Matching action type.
            commandList.get(index).execute(entity, deltaTime);

            return true;
        }
        return false;
    }

    /**
     * Registers a new game action with a given name.
     * @param name of the new action to register to this input handler.
     * @param action to register with this input handler.
     */
    public void associateAction(String name, InputActionType inputActionType, GameCommand action) {
        if(actionNames.add(name)) {
            logger.info("Binding a new action with an input handler Name:{} InputActionType: {} GameCommand: {}", name, inputActionType, action);
            actionTypeList.add(inputActionType);
            commandList.add(action);
        } else {
            logger.error("Tried to register additional action with duplicate name: {}", name);
        }
    }
}
