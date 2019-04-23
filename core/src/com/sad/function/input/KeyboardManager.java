package com.sad.function.input;

import com.badlogic.gdx.InputProcessor;
import com.sad.function.common.Subject;
import com.sad.function.event.KeyInputEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a keyboard. Will fire events at it's listeners. It's up to them to handle these events.
 */
public class KeyboardManager implements InputProcessor, Subject {
    private List<KeyState> keyStates = new ArrayList<>();

    public KeyboardManager() {
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

    /**
     * Push all down events into the key event queue and then onto the observers registered to this object.
     * @param delta
     */
    public void dispatch(float delta) {
        for(KeyState keyState : keyStates) {
            if(keyState.pressed || keyState.down ) {
                getObservers().forEach(observer -> observer.onNotify(new KeyInputEvent().setValue(1).setId(keyState.key)));
            }
            if(keyState.released) {
                getObservers().forEach(observer -> observer.onNotify(new KeyInputEvent().setValue(0).setId(keyState.key)));

            }
        }
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

    /**
     * Our reset method. A good place to actually store/dispatch a list of KeyStates for later reference. We're only
     * focusing on the current frame.
     */
    public void update() {
        for (int i = 0; i < 256; i++) {
            KeyState k = keyStates.get(i);
            k.pressed = false;
            k.released = false;
        }
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
