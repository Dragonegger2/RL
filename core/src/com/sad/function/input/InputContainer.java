package com.sad.function.input;

import java.util.ArrayList;
import java.util.List;

public class InputContainer {
    public List<InputManager.KeyState> keyStates;

    public InputContainer() {
        keyStates = new ArrayList<>();
    }


    public boolean isKeyPressed(int key) {
        return keyStates.get(key).pressed;
    }

    public boolean isKeyDown(int key) {
        return keyStates.get(key).down;
    }

    public boolean isKeyReleased(int key) {
        return keyStates.get(key).released;
    }

}
