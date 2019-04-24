package com.sad.function.input.devices;

import com.sad.function.event.Event;

import java.util.List;
import java.util.UUID;

public interface IDevice {
    List<Event> pollDevice();

    void updateDeviceId(UUID id);
    UUID getDeviceId();
    void clear();
}
