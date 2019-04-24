package com.sad.function.input.devices;

import com.badlogic.gdx.Gdx;
import com.sad.function.common.Subject;
import com.sad.function.components.InputHandler;
import com.sad.function.event.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 *
 */
public class DeviceManager implements Subject {
    private static final Logger logger = LogManager.getLogger(DeviceManager.class);

    private HashMap<UUID, IDevice> registeredDevices;
    private HashMap<UUID, InputHandler> registeredInputHandlers;

    public DeviceManager() {
        //Register a keyboard manager.
        registeredDevices = new HashMap<>();
        registeredInputHandlers = new HashMap<>();

        Keyboard keyboard = new Keyboard();

        registerDevice(keyboard.getDeviceId(), keyboard);

        //Register the keyboard to gdx's input structure.
        Gdx.input.setInputProcessor(keyboard);
    }

    /**
     * Add a device to this manager.
     * @param id of the device being registered.
     * @param device to be registered.
     */
    private void registerDevice(UUID id, IDevice device) {
        registeredDevices.put(id, device);
        logger.info("Registered device {} with id {}.", device, id);
    }

    private void registerInputHandler(UUID id, InputHandler inputHandler) {
        //Great, you've got a matching device and input handler.
        if(registeredDevices.keySet().contains(id)) {
            registeredInputHandlers.put(id, inputHandler);
        } else {
            logger.warn("No device matching that value has been registered yet. {}", id);
        }
    }

    public void assignDevice(InputHandler inputHandler) {
        if(listAvailableDevices().size() > 0) {
            UUID availableId = listAvailableDevices().get(0);

            registerInputHandler(availableId, inputHandler);
            inputHandler.setDeviceId(availableId);

            logger.info("Device was assigned for: {}", inputHandler);
        } else {
            logger.warn("No extra devices are currently available.");
        }

    }
    /**
     * Fetch the underlying device.
     * @param id of the registered device.
     * @return list of events from the device.
     */
    public List<Event> pollDevice(UUID id) {
        IDevice device = registeredDevices.get(id);
        if(device != null) {
            //return underlying event queue.
            return device.pollDevice();
        }
        return Collections.emptyList();
    }

    /**
     * Compares the available device ids with the registered ids to get all available ids.
     * @return list of available device ids.
     */
    public List<UUID> listAvailableDevices() {
        Set<UUID> deviceIds = registeredDevices.keySet();
        for(UUID assignedId : registeredInputHandlers.keySet()) {
            deviceIds.remove(assignedId);
        }

        return new ArrayList<>(deviceIds);
    }

    /**
     * Clear out any potential state changes on the devices. IE. pressed, released, etc.
     */
    public void clearDeviceQueues() {
        for(UUID key: registeredDevices.keySet()) {
            registeredDevices.get(key).clear();
        }
    }
}
