package com.sad.function.input;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

public class InputManager implements InputProcessor {
    private List<KeyState> keyStates = new ArrayList<>();

    public InputManager() {
        for (int i = 0; i < 256; i++) {
            keyStates.add(new KeyState(i));
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keyStates.get(keycode).pressed = true;
        keyStates.get(keycode).down = true;

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyStates.get(keycode).down = false;
        keyStates.get(keycode).released = true;

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

    public boolean isKeyPressed(int key) {
        return keyStates.get(key).pressed;
    }

    public boolean isKeyDown(int key) {
        return keyStates.get(key).down;
    }

    public boolean isKeyReleased(int key) {
        return keyStates.get(key).released;
    }

    /**
     * Our reset method. A good place to actually store/update a list of KeyStates for later reference. We're only
     * focusing on the current frame.
     */
    public void update() {
        for (int i = 0; i < 256; i++) {
            KeyState k = keyStates.get(i);
            k.pressed = false;
            k.released = false;
        }
    }

    /**
     * @return the current keyboard state.
     */
    public List<KeyState> getKeyboardState() {
        return keyStates;
    }

    class InputState {
        boolean pressed = false;
        boolean down = false;
        boolean released = false;
    }

    public class KeyState extends InputState {
        public int key;

        KeyState(int key) {
            this.key = key;
        }
    }
}
