package com.sad.function.input.keyboard;

import com.sad.function.common.SizedStack;

public class KeyboardState {
    private SizedStack<Keyboard> keyboardStack;

    public KeyboardState(int stackSize) {
        keyboardStack = new SizedStack<>(stackSize);

        // Push two empty keyboard states to prevent it from
        // throwing errors checking for
        keyboardStack.push(new Keyboard());
        keyboardStack.push(new Keyboard());
    }

    /**
     * Check if the keyStates is currently down.
     * @param key
     * @return
     */
    public boolean isKeyDown(int key) {
        return keyboardStack.getHead().isKeyDown(key);
    }

    /**
     * Check if the keyStates is currently up.
     * @param key
     * @return
     */
    public boolean isKeyUp(int key) {
        return keyboardStack.getHead().isKeyUp(key);
    }

    /**
     * Last tick the keyStates was up and t his tick it's now down.
     * @param key to check the state of.
     * @return if the keyStates was just pressed.
     */
    public boolean isKeyJustPressed(int key) {
        return keyboardStack.getHead().isKeyDown(key) && keyboardStack.get(1).isKeyUp(key);
    }

    /**
     * Last tick the keyStates was down and this tick it's now up.
     * @param key to check the state of.
     * @return if the keyStates was just released.
     */
    public boolean isKeyJustReleased(int key) {
        return keyboardStack.getHead().isKeyUp(key) && keyboardStack.get(1).isKeyDown(key);
    }

    /**
     * Add additional keyboard state to the stack.
     * @param keyboard
     */
    public void pushKeyboardState(Keyboard keyboard) {
        keyboardStack.push(keyboard);
    }
}
