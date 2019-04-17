package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Action {
    @JsonProperty("name")
    private InputConstants.Action name;
    @JsonProperty("keys")
    private Set<Integer> keys;

    public Action() {}

    public Action(InputConstants.Action name, Set<Integer> keys) {
        this.name = name;
        this.keys = keys;
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
}
