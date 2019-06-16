package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

public abstract class Shape {
    protected Fixture parent;

    public abstract Vector2 getOrigin();
    public abstract Vector2 getVertex(int i, Transform t, Vector2 axis);
    public abstract Vector2[] getAxes(Transform transform);
    public abstract int getNumberOfVertices();


    /**
     * Returns the vector that is perpendicular to the provided one.
     * @return the perpendicular vector.
     */
    public static Vector2 normal(Vector2 v) {
        return new Vector2(-1 * v.y, v.x);
    }

    public void setParentFixture(Fixture f) { this.parent = f; }
    public Fixture getParentFixture() { return parent; }
    public boolean hasParentFixture() { return parent != null; }

}
