package com.sad.function.input.definitions;

/**
 * Raw input from the system.
 * Buttons can map to States or Actions,
 * Axes always map to ranges.
 */
public class RawInputConstants {
    public enum RawInputButton {
        W,
        S,
        D,
        A,
        SPACE
    }

    public enum RawInputAxis {
        RAW_INPUT_AXIS_MOUSE_X,
        RAW_INPUT_AXIS_MOUSE_Y,
    }

    public enum Range {
        RANGE_ONE,
    }
}
