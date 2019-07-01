package com.sad.function.collision.overlay.geometry;

import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;

public class RaycastResult implements Comparable<RaycastResult> {
    private Body body;
    private BodyFixture fixture;
    private Raycast raycast;

    public RaycastResult(Body body, BodyFixture fixture, Raycast raycast) {
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

    public BodyFixture getFixture() {
        return fixture;
    }

    public void setFixture(BodyFixture fixture) {
        this.fixture = fixture;
    }

    public Raycast getRaycast() {
        return raycast;
    }

    public void setRaycast(Raycast raycast) {
        this.raycast = raycast;
    }
}
