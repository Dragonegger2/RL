package com.sad.function.input.devices;

import com.sad.function.event.input.InputEvent;

import java.util.List;
import java.util.UUID;

public interface IDevice {
    List<InputEvent> pollDevice();

    void updateDeviceId(UUID id);
    UUID getDeviceId();
    void clear();
}
