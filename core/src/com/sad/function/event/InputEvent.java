package com.sad.function.event;

public class InputEvent extends Event {
    private boolean up;
    private boolean down;
    private int keycode;

    public InputEvent() {
        super(EventType.INPUT);
    }

    public InputEvent(int keycode, boolean up, boolean down) {
        super(EventType.INPUT);
        this.up = up;
        this.down = down;
        this.keycode = keycode;
    }

    public int getKeycode() {
        return keycode;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isUp() {
        return up;
    }
}
