package com.sad.function.input.definitions;

public class Keyboard {
    private static boolean[] down = new boolean[256];

    public boolean isKeyDown(int key) { return down[key]; }
    public boolean isKeyUp(int key) { return !down[key]; }

    public void setKeyDown(int key) {
        down[key] = true;
    }

    public void setKeyUp(int key) {
        down[key] = false;
    }
}
