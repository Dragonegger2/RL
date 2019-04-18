package com.sad.function.common;

import java.util.ArrayList;

public abstract class Subject<T> {
    private ArrayList<Observer<T>> observers = new ArrayList<>();
    private int numberOfObservers;

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
        numberOfObservers++;
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
        numberOfObservers--;
    }

    public ArrayList<Observer<T>> getObservers() {
        return observers;
    }
}
