package com.sad.function.common;

import java.util.Stack;

public class SizedStack<T> extends Stack<T> {
    private final int maxSize;

    public SizedStack(int size) {
        super();
        this.maxSize = size;
    }

    @Override
    public T push(T object) {
        while(this.size() >= maxSize) {
            this.remove(0);
        }

        return super.push(object);
    }

    public T getHead() {
        return super.get(super.size() - 1);
    }

    public T get(int index) {
        return super.get(index);
    }
}
