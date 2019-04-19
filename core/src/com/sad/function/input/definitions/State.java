package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class State {
    @JsonProperty("name")
    private InputConstants.State name;
    @JsonProperty("keys")
    private Set<Integer> keys;

    private boolean active;

    public State() {}

    public State(InputConstants.State name, Set<Integer> keys, boolean active) {
        this.name = name;
        this.keys = keys;
        this.active = active;
    }

    public InputConstants.State getName() {
        return name;
    }

    public Set<Integer> getKeys() {
        return keys;
    }

    public boolean containsKey(int searchKey) {
        return keys.contains(searchKey);
    }

    /**
     * Returns true when the button is pressed, false when it is not.
     * @return whether this state is currently pressed down.
     */
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("STATE Name: %s Keys %s", name, keys);
    }
}