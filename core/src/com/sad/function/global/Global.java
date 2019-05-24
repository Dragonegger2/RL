package com.sad.function.global;

import com.sad.function.event.GlobalEventQueue;
import com.sad.function.input.InputEventPool;

public class Global {
    public static GlobalEventQueue eventQueue = new GlobalEventQueue();

    public static InputEventPool inputEventPool = new InputEventPool(100);

    public static final float MAX_MOVEMENT_SPEED = 160f;

    public static final boolean DEBUG = true;

    public static final float PPM = 32f; // 100 pixels per meter.
}