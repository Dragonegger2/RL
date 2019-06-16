package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public abstract class AbstractShape extends Shape {
    protected final UUID id = UUID.randomUUID();

    protected Vector2 center;

    protected float radius;

    protected AbstractShape(float radius) {
        this(new Vector2(), radius);
    }

    protected AbstractShape(Vector2 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Id=" + this.id +
                "|Center=" + this.center +
                "|Radius=" + this.radius;
    }

    public UUID getId() {
        return this.id;
    }

    public Vector2 getCenter() {
        return this.center;
    }

    public void translate(float x, float y) {
        this.center.add(x, y);
    }

    public void setRadius(Vector2 vector) {
        this.translate(vector.x, vector.y);
    }

    public Projection projection(Vector2 n) {
        return this.project(n, Transform.IDENTITY);
    }
}
