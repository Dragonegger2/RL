package com.sad.function.global;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.sad.function.event.GlobalEventQueue;
import com.sad.function.input.devices.DeviceManager;

public class Global {
    /**
     * Loaded textures.
     */
    public static Textures textures = new Textures();

    public static Entity globalEntity = new Entity();

    public static DeviceManager deviceManager = new DeviceManager();

    public static GlobalEventQueue eventQueue = new GlobalEventQueue();

    public static AssetManager assetManager = new AssetManager();

    public static final float MAX_MOVEMENT_SPEED = 80;
    public static final float MIN_MOVEMENT_SPEED = -80;
}

