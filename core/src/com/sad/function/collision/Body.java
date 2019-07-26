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
    private Vector2 velocity;
    private boolean isStatic = false;
    private String tag;
    private List<Fixture> fixtures;
    private float gravityScale = 1.0f;

    private Object userData;

    public Body() {
        velocity = new Vector2();
        color = Color.RED;
        tag = "UNSET";

        fixtures = new ArrayList<>(1);
        id = UUID.randomUUID();
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean flag) { isStatic = flag; }

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
        Fixture fixture = new Fixture(convex);
        fixtures.add(fixture);
        return fixture;
    }

    /**
     * Remove the provided {@link Fixture} from this body.
     * @param fixture to remove.
     * @return if successfully removed the fixture.
     */
    public boolean removeFixture(Fixture fixture) {
        return fixtures.remove(fixture);
    }

    /**
     * Remove the {@link Fixture} at a given index.
     * @param index to remove {@link Fixture} from
     * @return the removed {@link Fixture}.
     */
    public Fixture removeFixture(int index) {
        return fixtures.remove(index);
    }

    /**
     * Remove all associated fixtures.
     */
    public void removeFixtures() { fixtures.clear(); }

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

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
