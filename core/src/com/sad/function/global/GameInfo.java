package com.sad.function.global;

public class GameInfo {
    public static final boolean DEBUG = true;

    public static final float MAX_MOVEMENT_SPEED = 160f;

    //The best value seems to be somewhere between 6 & 12.
    public static final float VIRTUAL_HEIGHT = 24f;

    public static boolean RENDER_SPRITES = true;
    public static boolean RENDER_SPRITE_OUTLINES = true;
    public static boolean RENDER_HITBOX_OUTLINES = true;

    public static int PLAYER;
    public static final float XVelocity = 2f;

    public static int FOOT_CONTACTS = 0;

    public enum FIXTURE_TYPE {
        FOOT
    }

    public static float GRAVITY = 9.8f;
    public static float MAX_FALL_SPEED = 15f;

    public static final int VELOCITY_CONSTRAINT_SOLVER_TERATIONS = 6;
    public static final int POSITION_CONSTRAINT_SOLVER_TERATIONS = 8;

    public static final float DEFAULT_SLEEP_ANGULAR_VELOCITY = (float)Math.toRadians(2.0);
    public static final float DEFAULT_SLEEP_ANGULAR_VELOCITY_SQUARED = DEFAULT_SLEEP_ANGULAR_VELOCITY * DEFAULT_SLEEP_ANGULAR_VELOCITY;

    public static final float DEFAULT_STEP_FREQUENCY = 1.0f / 60.0f;
}