package com.sad.function.input;

import com.sad.function.event.input.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputEventPool {
    private static final Logger logger = LogManager.getLogger(InputEventPool.class);

    private int POOL_SIZE = 100;

    private InputEvent[] inputEventPool = new InputEvent[POOL_SIZE];

    public InputEventPool() {
        this(100);
    }

    public InputEventPool(int pool_size) {
        for(int i = 0; i < pool_size; i++) {
            inputEventPool[i] = new InputEvent();
        }
    }

    public InputEvent create(int id, float value) {
        for(int i = 0; i < POOL_SIZE; i++) {
            if(!inputEventPool[i].inUse) {
                inputEventPool[i].initialize(id, value);
                inputEventPool[i].inUse = true;

                return inputEventPool[i];
            }
        }

        logger.warn("No available input event in the pool. May need to increase pool size if this continues.");
        return null;
    }


}
