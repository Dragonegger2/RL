package com.sad.function.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sad.function.input.definitions.InputConstants;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contexts {
    public Map<InputConstants.Contexts, InputContext> contexts;
}
