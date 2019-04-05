package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Iterator;
import java.util.Set;

public class Action {
    @JsonProperty("name")
    private InputConstants.Action name;
    @JsonProperty("keys")
    private Set<RawInputConstants.RawInputButton> keys;

    public Action() {}

    public Action(InputConstants.Action name, Set<RawInputConstants.RawInputButton> keys) {
        this.name = name;
        this.keys = keys;
    }

    public InputConstants.Action getName() {
        return name;
    }

    public Set<RawInputConstants.RawInputButton> getKeys() {
        return keys;
    }

    public boolean containsKey(RawInputConstants.RawInputButton searchKey) {
        return keys.contains(searchKey);
    }
}
