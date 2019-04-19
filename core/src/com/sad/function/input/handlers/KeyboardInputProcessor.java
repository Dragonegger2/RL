package com.sad.function.input.handlers;

import com.badlogic.gdx.InputProcessor;
import com.sad.function.common.SizedStack;
import com.sad.function.input.keyboard.Keyboard;

/**
 * This input processor handles all keyboard presses as simple as two events: up or down.
 * <p>
 * This is used by a higher level system to make determinations to fire events based on states or actions of the keys.
 * <p>
 * It is registered as an input processor during game instantiation.
 */
public class KeyboardInputProcessor implements InputProcessor {
    private static Keyboard currentKeyboardStatus = new Keyboard();

    private static SizedStack<Keyboard> keyboardStates = new SizedStack<>(2);

    @Override
    public boolean keyDown(int key) {
        currentKeyboardStatus.setKeyDown(key);

        return false;
    }

    @Override
    public boolean keyUp(int key) {
        currentKeyboardStatus.setKeyUp(key);

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public Keyboard getCurrentKeyboardStatus() { return currentKeyboardStatus; }
}
