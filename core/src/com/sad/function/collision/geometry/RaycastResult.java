package com.sad.function.collision.geometry;


import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;

public class RaycastResult implements Comparable<RaycastResult> {
    private Body body;
    private Fixture fixture;
    private Raycast raycast;

    public RaycastResult(Body body, Fixture fixture, Raycast raycast) {
        this.body = body;
        this.fixture = fixture;
        this.raycast = raycast;
    }

    @Override
    public int compareTo(RaycastResult o) {
        return (int) Math.signum(this.raycast.getDistance() - o.raycast.getDistance());
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public Raycast getRaycast() {
        return raycast;
    }

    public void setRaycast(Raycast raycast) {
        this.raycast = raycast;
    }
}
