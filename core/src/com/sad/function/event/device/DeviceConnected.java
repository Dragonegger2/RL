package com.sad.function.event.device;

import com.sad.function.event.Event;
import com.sad.function.event.EventType;
import com.sad.function.input.devices.IDevice;

public class DeviceConnected extends Event {
    private IDevice device;

    public DeviceConnected() {
        super(EventType.NEW_DEVICE_CONNECTED);
    }

    public IDevice getDevice() { return device; }

    public DeviceConnected setDevice(IDevice device) {
        this.device = device;
        return this;
    }
}
