package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.AABB;
import com.sad.function.collision.overlay.Translatable;
import com.sad.function.collision.overlay.broadphase.Collidable;
import com.sad.function.collision.overlay.container.Fixture;
import com.sad.function.collision.overlay.data.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class AbstractCollidable<T extends Fixture> implements Collidable<T>, Translatable {
    protected final UUID id;
    protected Transform transform;
    protected List<T> fixtures;
    protected float radius;
    protected float angularVelocity;
    protected String tag;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    //region Fixture Manipulation Logic
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

    //region Translation
    @Override
    public void translate(float x, float y) {
        this.transform.translate(x, y);
    }
    //endregion

    @Override
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
    //endregion

    public int getFixtureCount() {
        return this.fixtures.size();
    }

    public List<T> getFixtures() {
        return Collections.unmodifiableList(this.fixtures);
    }

    public Transform getTransform() {
        return this.transform;
    }

    @Override
    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    @Override
    public AABB createAABB() {
        return createAABB(this.transform);
    }

    @Override
    public AABB createAABB(Transform transform) {
        int size = fixtures.size();
        if (size > 0) {
            AABB aabb = fixtures.get(0).getShape().createAABB(transform);

            for (int i = 1; i < size; i++) {
                AABB faabb = fixtures.get(i).getShape().createAABB(transform);
                aabb.union(faabb);
            }

            return aabb;
        }

        return new AABB(0, 0, 0, 0);
    }

    public UUID getId() {
        return this.id;
    }

    @Override
    public float getRotationDiscRadius() {
        return this.radius;
    }

    @Override
    public Vector2 getLocalPoint(Vector2 worldPoint) {
        return transform.getInverseTransformed(worldPoint);
    }

    @Override
    public Vector2 getWorldPoint(Vector2 localPoint) {
        return transform.getTransformed(localPoint);
    }

    @Override
    public Vector2 getLocalVector(Vector2 worldVector) {
        return transform.getInverseTransformedR(worldVector);
    }

    @Override
    public Vector2 getWorldVector(Vector2 localVector) {
        return transform.getTransformedR(localVector);
    }

    public void rotateAboutCenter(float theta) {
        Vector2 center = this.getWorldCenter();
        rotate(theta, center);
    }

    public void rotate(float theta, Vector2 point) {
        transform.rotate(theta, point);
    }

    public void rotate(float theta) {
        transform.rotate(theta);
    }
}
