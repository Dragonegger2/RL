package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Action {
    @JsonProperty("name")
    private InputConstants.Action name;
    @JsonProperty("keys")
    private Set<Integer> keys;

    private FireState fireState = FireState.NONE;

    public Action() {}

    public Action(InputConstants.Action name, Set<Integer> keys, FireState fireState) {
        this.name = name;
        this.keys = keys;
        this.fireState = fireState;
    }

    public InputConstants.Action getName() {
        return name;
    }

    public Set<Integer> getKeys() {
        return keys;
    }

    public boolean containsKey(int searchKey) {
        return keys.contains(searchKey);
    }

    @Override
    public String toString() {
        return String.format("ACTION Name: %s Keys %s", name, keys);
    }

    /**
     * Returns the current state of this action based on it's firing state.
     * NONE - Not firing for any state.
     * PRESSED - was just pressed.
     * RELEASED - was just released.
     *
     * @return the current action state.
     */
    public FireState getButtonState() { return fireState; }

    public enum FireState {NONE, PRESSED, RELEASED}
}
