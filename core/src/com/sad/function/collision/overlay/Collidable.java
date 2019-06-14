package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Collidable {
    private Vector2 position;
    private List<Body> bodies;

    public Collidable(float x, float y) {
        position = new Vector2(x, y);

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
}
