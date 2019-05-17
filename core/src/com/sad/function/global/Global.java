package com.sad.function.global;

import com.sad.function.event.GlobalEventQueue;

public class Global {
    public static GlobalEventQueue eventQueue = new GlobalEventQueue();

    public static final float MAX_MOVEMENT_SPEED = 160f;
}