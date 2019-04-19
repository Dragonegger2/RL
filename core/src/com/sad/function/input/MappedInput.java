package com.sad.function.input;

import com.sad.function.input.definitions.Action;
import com.sad.function.input.definitions.InputConstants;
import com.sad.function.input.definitions.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Object that represents all mapped input for a given tick.
 */
public class MappedInput {
    private Map<InputConstants.Action, Action> actions;
    private Map<InputConstants.State, State> states;

    public MappedInput() {
        actions = new HashMap<>();
        states = new HashMap<>();
    }

    public void addAction(Action action) {
        actions.put(action.getName(), action);
    }

    public void addState(State state) {
        states.put(state.getName(), state);
    }

    public void eatAction(InputConstants.Action action) {
        actions.remove(action);
    }

    public void eatState(InputConstants.State state) {
        states.remove(state);
    }


    public Map<InputConstants.Action, Action> getActions() { return actions; }
    public Map<InputConstants.State, State> getStates() { return states; }

    /**
     * Check the given state in the mapped input and see if it has been pressed.
     *
     * @param stateName
     * @return whether the given state has been pressed.
     */
    public boolean isPressed(InputConstants.State stateName) {
        State state = states.get(stateName);
        if(state != null ) {
            return state.isActive();
        }
        return false;
    }
}