package com.sad.function.input.definitions;

/**
 * All actions, states and ranges defined in the system.
 */
public class InputConstants {
    public enum Contexts {
        TEST,
        GAME
    }

    public enum Action {
        FIRE,
    }

    public enum State {
        PLAYER_UP,
        PLAYER_DOWN,
        PLAYER_LEFT,
        PLAYER_RIGHT
    }

}
