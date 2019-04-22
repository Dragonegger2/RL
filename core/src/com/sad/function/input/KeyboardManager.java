package com.sad.function.input;

import com.badlogic.gdx.InputProcessor;
import com.sad.function.common.Subject;
import com.sad.function.input.events.InputEvent;
import com.sad.function.input.events.KeyInputEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a keyboard. Will fire events at it's listeners. It's up to them to handle things.
 */
public class KeyboardManager extends Subject<List<InputEvent>> implements InputProcessor {
    private List<KeyState> keyStates = new ArrayList<>();

    private List<InputEvent> keyInputEvents = new ArrayList<>();

    public KeyboardManager() {
        for (int i = 0; i < 256; i++) {
            keyStates.add(new KeyState(i));
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        //fire a key pressed.
        keyStates.get(keycode).pressed = true;
        keyStates.get(keycode).down = true;

        keyInputEvents.add(new KeyInputEvent().setValue(1).setId(keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyStates.get(keycode).down = false;
        keyStates.get(keycode).released = true;

        //Push a new released event.
        keyInputEvents.add(new KeyInputEvent().setValue(0).setId(keycode));

        return false;
    }

    /**
     * Push all down events into the key event queue and then onto the observers registered to this object.
     * @param delta
     */
    public void update(float delta) {
        for(KeyState keyState : keyStates) {
            if(keyState.down) {
                keyInputEvents.add(new KeyInputEvent().setValue(0).setId(keyState.key));
            }
        }

        getObservers().forEach(listObserver -> listObserver.onNotify(keyInputEvents));
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

        keyInputEvents.clear();
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
