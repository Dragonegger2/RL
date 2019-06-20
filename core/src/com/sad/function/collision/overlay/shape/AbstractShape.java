package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Projection;
import com.sad.function.collision.overlay.data.Transform;

import java.util.UUID;

public abstract class AbstractShape implements Shape {
    protected final UUID id = UUID.randomUUID();

    protected Vector2 center;
    protected float radius;

    protected AbstractShape(float radius) {
        this(new Vector2(), radius);
    }

    public AbstractShape(Vector2 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id=").append(this.id)
                .append("|Center=").append(this.center)
                .append("|Radius=").append(this.radius);
        return sb.toString();
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public Vector2 getCenter() {
        return this.center;
    }

    @Override
    public float getRadius() {
        return this.radius;
    }

    public void translate(float x, float y) {
        this.center.add(x, y);
    }

    public void translate(Vector2 vector) {
        this.translate(vector.x, vector.y);
    }

    @Override
    public boolean contains(Vector2 point) {
        return this.contains(point, Transform.IDENTITY);
    }

    @Override
    public Projection project(Vector2 n) {
        return this.project(n, Transform.IDENTITY);
    }

    public abstract Vector2[] getVertices();
    public abstract Vector2[] getNormals();
}