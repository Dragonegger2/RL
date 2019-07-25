package com.sad.function.collision;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Convex;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represented by a group of {@link com.sad.function.collision.shape.Shape}.
 */
public class Body {
    private final UUID id;
    private Color color;
    private Transform transform;
    private Transform transform0;
    private Vector2 velocity;
    private boolean isStatic = false;
    private String tag;
    private List<Fixture> fixtures;

    private Object userData;

    public Body() {
        transform = new Transform();
        velocity = new Vector2();
        color = Color.RED;
        tag = "UNSET";

        fixtures = new ArrayList<>(1);
        id = UUID.randomUUID();
    }

    public Transform getInitialTransform() {
        return transform0;
    }

    public Transform getTransform() {
        return transform;
    }

    public void translate(Vector2 t) {
        translate(t.x, t.y);
    }

    public void translate(float x, float y) {
        transform.translate(x, y);
    }

    public Vector2 getPosition() {
        return new Vector2(transform.x, transform.y);
    }

    public float getX() {
        return transform.x;
    }

    public float getY() {
        return transform.y;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean flag) {
        isStatic = false;
    }

    //region fixture handling.

    public List<Fixture> getFixtures() {
        return fixtures;
    }
    public Fixture getFixture(int index) { return fixtures.get(index); }
    public int getFixtureCount() {
        return fixtures.size();
    }

    public void addFixture(Fixture fixture) {
        fixtures.add(fixture);
    }

    public Fixture addFixture(Convex convex) {
        return addFixture(convex, "DEFAULT_TAG");
    }

    public Fixture addFixture(Convex convex, String fixtureTag) {
        Fixture fixture = new Fixture(convex, fixtureTag);
        fixtures.add(fixture);
        return fixture;
    }

    public boolean removeFixture(Fixture fixture) {
        return fixtures.remove(fixture);
    }

    public Fixture removeFixture(int index) {
        return fixtures.remove(index);
    }

    //endregion

    /**
     * Generated object for contacts. Used to maintain relationships between touching objects.
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public Object getUserData() {
        return userData;
    }

    @Override
    public int hashCode() { return 17 * id.hashCode(); }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() { return this.color; }

    public UUID getId() { return id; }
}
