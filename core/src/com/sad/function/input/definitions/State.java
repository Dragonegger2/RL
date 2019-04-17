package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class State {
    @JsonProperty("name")
    private InputConstants.State name;
    @JsonProperty("keys")
    private Set<Integer> keys;

    public State() {}

    public State(InputConstants.State name, Set<Integer> keys) {
        this.name = name;
        this.keys = keys;
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

}