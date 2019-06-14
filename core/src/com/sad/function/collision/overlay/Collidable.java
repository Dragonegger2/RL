package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Collidable {
    private Vector2 position;
    private Vector2 velocity;

    private List<Body> bodies;

    public Collidable(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2();

        bodies = new ArrayList<>();
    }
    public Vector2 getPosition() { return position; }

    public Body createBody(Body b) {
        bodies.add(b);
        b.setParent(this);

        return b;
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void impulse(float x, float y) {
        this.velocity.add(x, y);
    }
}
