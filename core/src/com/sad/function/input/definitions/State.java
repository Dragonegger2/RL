package com.sad.function.input.definitions;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class State {
    @JsonProperty("name")
    private String name;
    @JsonProperty("keys")
    private int[] keys;

    public State() {}

    public State(String name, int[] keys) {
        this.name = name;
        this.keys = keys;
    }

    public String getName() {
        return name;
    }

    public int[] getKeys() {
        return keys;
    }

    /**
     * Matches all keys bound to this action for the given context.
     * @return any of the keys that are pressed.
     */
    public int[] isKeyPressed() {
        return Arrays.stream(keys).filter(key -> Gdx.input.isKeyPressed(key)).toArray();
    }

    public int[] isKeyJustPressed() {
        return Arrays.stream(keys).filter(key -> Gdx.input.isKeyJustPressed(key)).toArray();
    }
}
