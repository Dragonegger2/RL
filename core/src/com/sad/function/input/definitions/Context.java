package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an input context. It's a collection of actions, states, and ranges that this context cares about.
 */
public class Context {
    public Context() {}
    @JsonProperty("name")
    public InputConstants.Contexts contextName;

    @JsonProperty("actions")
    public List<Action> actions = new ArrayList<>();

    @JsonProperty("states")
    public List<State> states = new ArrayList<>();

    /**
     * Finds any actions that
     * @param button
     * @return
     */
    public List<Action> mapButtonToAction(RawInputConstants.RawInputButton button) {
        List<Action> firedActions = new ArrayList<>();
        for (Action action : actions) {
            if (action.containsKey(button)) {
                firedActions.add(action);
            }
        }
        return firedActions;
    }

    public List<State> mapButtonToState(RawInputConstants.RawInputButton button) {
        List<State> firedState = new ArrayList<>();
        for (State state : states) {
            if(state.containsKey(button)) {
                firedState.add(state);
            }
        }
        return firedState;
    }
}
