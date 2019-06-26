package com.sad.function.collision.overlay.collision.broadphase;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.AABB;
import com.sad.function.collision.overlay.container.Fixture;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.Convex;

import java.util.List;
import java.util.UUID;

public interface Collidable<T extends Fixture> {
    UUID getId();
    AABB createAABB();
    AABB createAABB(Transform transform);
    Collidable<T> addFixture(T fixture);
    T addFixture(Convex convex);
    T getFixture(int index);
    boolean containsFixture(T fixture);
    boolean removeFixture(T fixture);
    T removeFixture(int index);
    List<T> removeAllFixtures();
    int getFixtureCount();
    List<T> getFixtures();
    Vector2 getLocalCenter();
    Vector2 getWorldCenter();
    Vector2 getLocalPoint(Vector2 worldPoint);
    Vector2 getWorldPoint(Vector2 localPoint);
    Vector2 getLocalVector(Vector2 worldVector);
    Vector2 getWorldVector(Vector2 localVector);
    float getRotationDiscRadius();
    Transform getTransform();
    void setTransform(Transform transform);
    void rotateAboutCenter(float theta);
    void translateToOrigin();
}
