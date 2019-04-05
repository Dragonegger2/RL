package com.sad.function.input.definitions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("InputContext")
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputContext {
    @JsonProperty("actions")
    public List<Action> actions;
}
