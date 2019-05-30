package com.sad.function.global;

import com.sad.function.event.GlobalEventQueue;
import com.sad.function.input.InputEventPool;

public class GameInfo {
    public static final boolean DEBUG = true;

    public static final float MAX_MOVEMENT_SPEED = 160f;

    public static final float VIRTUAL_HEIGHT = 6f;

    public static InputEventPool inputEventPool = new InputEventPool(100);

    public static boolean RENDER_SPRITES = true;
    public static boolean RENDER_SPRITE_OUTLINES = true;
    public static boolean RENDER_HITBOX_OUTLINES = true;

    public static int PLAYER;
    public static final float XVelocity = 2f;
}