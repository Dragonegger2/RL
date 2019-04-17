package com.sad.function.input;

/**
 * Simple implementation of the InputChain of Responsibility pattern.
 */
public interface InputChain {
    void process(MappedInput input);
    void setNext(InputChain nextInChain);
}
