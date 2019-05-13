package com.sad.function;

import com.sad.function.event.input.InputEvent;

//TODO Create an input event pool to reuse the input events instead of creating new ones every single time.
public class InputEventPool {
    private int POOL_SIZE = 100;

    private InputEvent[] inputEventPool = new InputEvent[POOL_SIZE];

    public InputEventPool() {
        this(100);
    }

    public InputEventPool(int pool_size) {
        for(int i = 0; i < POOL_SIZE; i++) {
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

        return null;
    }


}
