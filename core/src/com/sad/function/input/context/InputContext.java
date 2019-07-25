package com.sad.function.input.context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Represents a context that maps input to actions.
 *
 * This determines allowed actions.
 */
public class InputContext {
    private static final Logger logger = LogManager.getLogger(InputContext.class);

    private HashMap<String, String> mappedInput;
    private String name;

    public InputContext(String name) {
        mappedInput = new HashMap<>();
        this.name = name;
    }

    /**
     * Find mapped input for this application.
     *
     * @param keyName to search for.
     * @return the action name for provided input for this given context.
     */
    public String findMappedInput(String keyName) {
        return mappedInput.get(keyName);
    }

    /**
     * Add a new mapping for this context.
     * @param KEY_NAME of the action.
     * @param ACTION_NAME for this context.
     */
    public void addMappedInput(String KEY_NAME, String ACTION_NAME) {
        mappedInput.put(KEY_NAME, ACTION_NAME);
        logger.info("Action bound to {}, KEY_NAME: {} ACTION_NAME: {}", name, KEY_NAME, ACTION_NAME);
    }

    public String getName() { return name; }
}
