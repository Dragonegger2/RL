package com.sad.function.input.keyboard;

public class Keyboard {
    private static boolean[] down = new boolean[256];
    private static boolean[] up = new boolean[256];

    public boolean isKeyDown(int key) { return down[key]; }
    public boolean isKeyUp(int key) { return up[key]; }

    public void setKeyDown(int key) {
        down[key] = true;
        up[key] = false;
    }

    public void setKeyUp(int key) {
        down[key] = false;
        up[key] = true;
    }
}
