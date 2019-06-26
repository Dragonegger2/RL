package com.sad.function.collision.overlay.data;

import com.badlogic.gdx.math.Vector2;

public class Force {
    private Vector2 force;

    public Force() {
        this.force = new Vector2();
    }

    public Force(Vector2 force) {
        this.force = force;
    }

    public Force(float x, float y) {
        this.force = new Vector2(x, y);
    }

    public Vector2 getForce() {
        return force;
    }

    public void setForce(Vector2 force) {
        this.force = force;
    }

    public void setForce(float x, float y) {
        this.force.set(x, y);
    }

    public boolean isComplete(float elapsedTime) {
        return true;
    }
}
