package com.sad.function.input;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sad.function.input.definitions.InputConstants;
import com.sad.function.input.definitions.InputContext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Creates a binding for contexts from input files.
 */
public class InputJsonReader {

    public static HashMap<InputConstants.Contexts, InputContext> readBindings() {
        TypeReference<HashMap<InputConstants.Contexts, InputContext>> CONTEXT_TO_ACTION =  new TypeReference<HashMap<InputConstants.Contexts, InputContext>>() {};
        HashMap<InputConstants.Contexts, InputContext> contextToAction = null;

        try {
            ObjectMapper mapper = new ObjectMapper();

            contextToAction = mapper.readValue(new File("./input/InputMap.json"), CONTEXT_TO_ACTION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contextToAction;
    }
}