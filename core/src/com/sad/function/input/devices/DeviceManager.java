package com.sad.function.input.devices;

import com.sad.function.common.Observer;
import com.sad.function.common.Subject;
import com.sad.function.components.InputHandler;
import com.sad.function.event.device.DeviceConnected;
import com.sad.function.event.Event;
import com.sad.function.event.EventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class DeviceManager implements Observer, Subject {
    private static final Logger logger = LogManager.getLogger(DeviceManager.class);

    private HashMap<UUID, IDevice> registeredDevices;
    private HashMap<UUID, InputHandler> registeredInputHandlers;

    public DeviceManager() {
        //Register a keyboard manager.
        registeredDevices = new HashMap<>();
        registeredInputHandlers = new HashMap<>();
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
     * Collects a list of devices that have input handlers.
     * @return the list of device that have input handlers.
     */
    public List<UUID> inUseDevices() {
        List<UUID> inUseDevices = new ArrayList<>();

        for(UUID id : registeredDevices.keySet()) {
            if(registeredInputHandlers.containsKey(id)) {
                inUseDevices.add(id);
            }
        }

        return inUseDevices;
    }

    /**
     * Clear out any potential state changes on the devices. IE. pressed, released, etc.
     */
    public void clearDeviceQueues() {
        for(UUID key: registeredDevices.keySet()) {
            registeredDevices.get(key).clear();
        }
    }

    @Override
    public void onNotify(Event event) {
        if(event.getEventType() == EventType.NEW_DEVICE_CONNECTED) {
            DeviceConnected deviceConnected = (DeviceConnected)event;
            this.registerDevice(deviceConnected.getDevice().getDeviceId(), deviceConnected.getDevice());
            logger.info("NEW DEVICE CONNECTED. {}", deviceConnected);
        }

        if(event.getEventType() == EventType.DEVICE_DISCONNECTED) {
            logger.info("DEVICE DISCONNECTED. {}", event);
        }
    }
}
