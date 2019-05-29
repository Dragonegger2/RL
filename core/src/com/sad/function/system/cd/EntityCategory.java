package com.sad.function.system.cd;

public enum EntityCategory {
    BOUNDARY(0x0001),
    PLAYER(0x0002),
    SENSOR(0x0004),
    GROUND(0x0008);

    public final int id;

    EntityCategory(int id) {
        this.id = id;
    }
}
