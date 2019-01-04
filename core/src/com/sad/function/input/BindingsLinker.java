package com.sad.function.input;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sad.function.input.definitions.InputConstants;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Creates a binding for contexts from input files.
 */
public class BindingsLinker {

    public static HashMap<InputConstants.Contexts, InputContext> readBindings() {
        TypeReference<HashMap<InputConstants.Contexts, InputContext>> typeRef = new TypeReference<HashMap<InputConstants.Contexts, InputContext>>() {};
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(new File("./input/InputMap.json"), typeRef);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ERROR CREATING CONTEXTS.");
        return new HashMap<>();
    }
}
