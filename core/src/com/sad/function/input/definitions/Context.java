package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sad.function.input.InputChain;
import com.sad.function.input.MappedInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an input context. It's a collection of actions, states, and ranges that this context cares about.
 *
 * If is part of a Chain of Responsibility object.
 *
 * TODO: Need to clean up what a context is.
 */
public class Context implements InputChain {
    //Store a reference to the next link in the input chain.
    private InputChain nextLink = null;

    public Context() {}
    @JsonProperty("name")
    public InputConstants.Contexts contextName;

    @JsonProperty("actions")
    private List<Action> actions = new ArrayList<>();

    @JsonProperty("states")
    private List<State> states = new ArrayList<>();

    /**
     * Finds any actions that match it from Raw InputHandler.
     * @param button
     * @return
     */
    public Action mapButtonToAction(int button) {
        for (Action action : actions) {
            if (action.containsKey(button)) {
                return action;
            }
        }
        return null;
    }

    public State mapButtonToState(int button) {
        for (State state : states) {
            if(state.containsKey(button)) {
                return state;
            }
        }
        return null;
    }

    @Override
    public void process(MappedInput input) {
        //Should handle the mappedinput, chomping any that it needs to before dispatching it to the next.
        if(nextLink != null) {
            nextLink.process(input);
        }
    }

    @Override
    public void setNext(InputChain nextInChain) {
        nextLink = nextInChain;
    }

    @Override
    public InputChain getNext() {
        return nextLink;
    }

    public List<Action> getContextActions() { return actions; }
    public List<State> getContextState() { return states; }
}
