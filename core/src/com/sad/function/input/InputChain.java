package com.sad.function.input;

/**
 * Simple implementation of the Chain of Responsibility pattern for the input handlers.
 */
public interface InputChain {
    void process(MappedInput input);
    void setNext(InputChain nextInChain);
    InputChain getNext();
}
