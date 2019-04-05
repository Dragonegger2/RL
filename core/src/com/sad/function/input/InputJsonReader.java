package com.sad.function.input;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sad.function.input.definitions.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a binding for contexts from input files.
 */
public class InputJsonReader {

    public static List<Context> readContextDefinitions() {
        ArrayList<Context> definedContexts = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Context>> CONTEXT_LIST = new TypeReference<List<Context>>() {};
            definedContexts =  mapper.readValue(new File("./input/InputMap.json"), CONTEXT_LIST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return definedContexts;
    }
}