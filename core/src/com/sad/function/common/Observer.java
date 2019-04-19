package com.sad.function.common;

public interface Observer<E> {
    /**
     * Notify this observer of a subject that they are registered to listen for. They will do their own message parsing.
     * @param event data to be handled.
     */
    void onNotify(E event);
}
