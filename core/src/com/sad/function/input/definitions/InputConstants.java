package com.sad.function.input.definitions;

/**
 * All actions, states and ranges defined in the system.
 */
public class InputConstants {
    public enum Contexts {
        TEST,
        GAME
    }

    //Single time event. Does not happen until the button is pressed again.
    public enum Action {
        PLAYER_UP,
        PLAYER_DOWN,
        PLAYER_LEFT,
        PLAYER_RIGHT
    }

    //Repeated event. Like shooting a gun.
    public enum State {
        SHOOT_GUN
    }

    public enum Range {}
}
