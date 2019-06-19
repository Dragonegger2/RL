package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.container.Fixture;
import com.sad.function.collision.overlay.data.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class AbstractCollidable<T extends Fixture> {
    protected final UUID id;
    protected Transform transform;
    protected List<T> fixtures;
    protected float radius;

    public AbstractCollidable() {
        this(1);
    }

    public AbstractCollidable(int fixtureCount) {
        int size = fixtureCount <= 0 ? 1 : fixtureCount;
        this.id = UUID.randomUUID();
        this.fixtures = new ArrayList<>(size);
        this.radius = 0.0f;
        this.transform = new Transform();
    }

    public boolean removeFixture(T fixture) {
        if (fixture == null)
            return false;

        int size = this.fixtures.size();
        if (size > 0) {
            return this.fixtures.remove(fixture);
        }

        return false;
    }

    public T removeFixture(int index) {
        return this.fixtures.remove(index);
    }

    public List<T> removeAllFixtures() {
        List<T> fixtures = this.fixtures;
        this.fixtures = new ArrayList<>(fixtures.size());

        return fixtures;
    }

    public boolean containsFixture(T fixture) {
        if (fixture == null)
            return false;
        return this.fixtures.contains(fixture);
    }

    public void translate(float x, float y) {
        this.transform.translate(x, y);
    }

    public void translate(Vector2 vector) {
        this.transform.translate(vector);
    }

    public void translateToOrigin() {
        Vector2 wc = new Vector2(0, 0);
        this.transform.translate(-wc.x, -wc.y);
    }

    public T getFixture(int index) {
        return this.fixtures.get(index);
    }

    public int getFixtureCount() {
        return this.fixtures.size();
    }

    public List<T> getFixtures() {
        return Collections.unmodifiableList(this.fixtures);
    }

    public Transform getTransform() {
        return this.transform;
    }

    public UUID getId() {
        return this.id;
    }
}
