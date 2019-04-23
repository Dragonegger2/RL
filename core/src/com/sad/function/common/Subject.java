package com.sad.function.common;

import java.util.ArrayList;

public interface Subject {
    ArrayList<Observer> observers = new ArrayList<>();

    default void addObserver(Observer observer) {
        observers.add(observer);
    }

    default void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    default ArrayList<Observer> getObservers() {
        return observers;
    }
}
