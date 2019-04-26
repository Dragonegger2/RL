package com.sad.function.input.devices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.sad.function.event.Event;
import com.sad.function.event.input.KeyInputEvent;
import com.sad.function.input.states.KeyState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a keyboard. Will fire events at it's listeners. It's up to them to handle these events.
 */
public class KeyboardDevice implements InputProcessor, IDevice {
    private UUID deviceId;

    private List<KeyState> keyStates = new ArrayList<>();

    public KeyboardDevice() {
        for (int i = 0; i < 256; i++) {
            keyStates.add(new KeyState(i));
        }

        deviceId = UUID.randomUUID();

        Gdx.input.setInputProcessor(this);
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

    @Override
    public List<Event> pollDevice() {
        List<Event> eventList = new ArrayList<>();

        for (KeyState keyState : keyStates) {
            if (keyState.pressed || keyState.down) {
                //TODO: Fix the bug that currently exists in the the values. We can't actually check pressed vs. constantly down. Need to add a field for relative/absolute.
                eventList.add(new KeyInputEvent().setValue(1).setId(keyState.key));
            }
            if (keyState.released) {
                eventList.add(new KeyInputEvent().setValue(0).setId(keyState.key));
            }
        }

        return eventList;
    }

    @Override
    public void updateDeviceId(UUID id) {
        this.deviceId = id;
    }

    @Override
    public UUID getDeviceId() {
        return deviceId;
    }

    /**
     * Our reset method. A good place to actually store/dispatch a list of KeyStates for later reference. We're only
     * focusing on the current frame.
     */
    @Override
    public void clear() {
        for (int i = 0; i < 256; i++) {
            KeyState k = keyStates.get(i);
            k.pressed = false;
            k.released = false;
        }
    }



}
