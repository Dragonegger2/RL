package com.sad.function.input.devices;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.sad.function.event.input.InputEvent;
import com.sad.function.input.states.ButtonState;
import com.sad.function.input.states.KeyState;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ControllerDevice implements ControllerListener, IDevice{
    //Dynamically register buttons/axis/etc?
    HashMap<Integer, ButtonState> buttons;
    HashMap<Integer, KeyState> axis;

    private UUID deviceId;
    private String name;

    public ControllerDevice() {
        buttons = new HashMap<>();
        axis = new HashMap<>();
    }

    @Override
    public void connected(Controller controller) {
        this.name = controller.getName();
        //assign uuid
        //send new connection
    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if(!buttons.containsKey(buttonCode)) {
            buttons.put(buttonCode, new ButtonState(buttonCode));
        }
        buttons.get(buttonCode).down = true;
        buttons.get(buttonCode).pressed = true;

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

    @Override
    public List<InputEvent> pollDevice() {
        //FIRE ALL MY EVENTS
        return null;
    }

    @Override
    public void updateDeviceId(UUID id) {
        this.deviceId = id;
    }

    @Override
    public UUID getDeviceId() {
        return deviceId;
    }

    @Override
    public void clear() {

    }
}
