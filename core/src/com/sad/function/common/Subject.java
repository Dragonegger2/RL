package com.sad.function.common;

import java.util.ArrayList;

public abstract class Subject {
    private ArrayList<Observer> observers = new ArrayList<>();
    private int numberOfObservers;

    public void addObserver(Observer observer) {
        observers.add(observer);
        numberOfObservers++;
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
        numberOfObservers--;
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }
}
