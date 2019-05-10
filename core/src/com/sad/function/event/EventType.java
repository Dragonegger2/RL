package com.sad.function.event;

public enum  EventType {
    /*
    These events cause the animation component to update it's state.
     */
    WALKING_LEFT,
    WALKING_RIGHT,
    WALKING_DOWN,
    WALKING_UP,

    //Device Handling
    NEW_DEVICE_CONNECTED,
    DEVICE_DISCONNECTED,

    //INPUT EVENTS
    INPUT,
    CLEAR_INPUT_QUEUE
}
