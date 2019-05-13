package com.sad.function.input.devices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.sad.function.InputEventPool;
import com.sad.function.event.input.InputEvent;
import com.sad.function.global.Global;
import com.sad.function.input.states.KeyState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a keyboard. Will fire events at it's listeners. It's up to them to handle these events.
 */
public class KeyboardDevice implements InputProcessor, IDevice {
    private UUID deviceId;
    private InputEventPool inputEventPool;

    private List<KeyState> keyStates = new ArrayList<>();

    public KeyboardDevice() {
        for (int i = 0; i < 256; i++) {
            keyStates.add(new KeyState(i));
        }

        deviceId = UUID.randomUUID();
        this.inputEventPool = Global.inputEventPool;

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
    public List<InputEvent> pollDevice() {
        List<InputEvent> eventList = new ArrayList<>();

        for (KeyState keyState : keyStates) {
            if (keyState.pressed || keyState.down) {
                 InputEvent event = inputEventPool.create(keyState.key, 1);
                 if(event != null) eventList.add(event);
            }
            if (keyState.released) {
                InputEvent event = inputEventPool.create(keyState.key, 0);
                if(event != null) eventList.add(event);
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
