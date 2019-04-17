package com.sad.function.input;

import com.sad.function.input.definitions.Action;
import com.sad.function.input.definitions.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Object that represents all mapped input for a given tick.
 */
public class MappedInput {
    private List<Action> actions;
    private List<State> states;

    public MappedInput() {
        actions = new ArrayList<>();
        states = new ArrayList<>();
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addState(State state) {
        states.add(state);
    }

    public void eatAction(Action action) {
        actions.remove(action);
    }

    public void eatState(State state) {
        states.remove(state);
    }

    public List<Action> getActions() { return actions; }
    public List<State> getStates() { return states; }
}