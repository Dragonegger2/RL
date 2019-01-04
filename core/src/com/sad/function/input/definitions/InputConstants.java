package com.sad.function.input.definitions;

/**
 * All actions, states and ranges defined in the system.
 */
public class InputConstants {
    //Single time event. Does not happen until the button is pressed again.
    public enum Contexts {
        TEST,
        GAME
    }

    public enum Action {
        PLAYER_UP,
        PLAYER_DOWN,
        PLAYER_LEFT,
        PLAYER_RIGHT
    }

    //Repeated event. Like shooting a gun.
    public enum State {}

    public enum Range {}
}
