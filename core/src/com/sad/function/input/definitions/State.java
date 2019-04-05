package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class State {
    @JsonProperty("name")
    private InputConstants.State name;
    @JsonProperty("keys")
    private Set<RawInputConstants.RawInputButton> keys;

    public State() {}

    public State(InputConstants.State name, Set<RawInputConstants.RawInputButton> keys) {
        this.name = name;
        this.keys = keys;
    }

    public InputConstants.State getName() {
        return name;
    }

    public Set<RawInputConstants.RawInputButton> getKeys() {
        return keys;
    }

    public boolean containsKey(RawInputConstants.RawInputButton searchKey) {
        return keys.contains(searchKey);
    }

}