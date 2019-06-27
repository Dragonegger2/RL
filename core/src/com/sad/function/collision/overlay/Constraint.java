package com.sad.function.collision.overlay;

import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.data.UserData;

import java.util.UUID;

public abstract class Constraint {
    protected final Body body1;
    protected final Body body2;

    boolean onIsland;

    public Constraint(Body body1, Body body2) {
        this.body1 = body1;
        this.body2 = body2;
        onIsland = false;
    }

    public Body getBody1() { return body1; }
    public Body getBody2() { return body2; }

    void setOnIsland(boolean onIsland) {
        this.onIsland = onIsland;
    }

    boolean isOnIsland() {
        return onIsland;
    }
}
