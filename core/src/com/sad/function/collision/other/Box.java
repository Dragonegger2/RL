package com.sad.function.collision.other;

import com.badlogic.gdx.math.Vector2;

public class Box implements IPolygon {
    private Vector2 position;
    private Vector2[] vertices;
    private Vector2 halfsize;

    public Box(Vector2 position, Vector2 halfsize) {
        this.position = position;

        vertices = new Vector2[4];
        vertices[0] = new Vector2();
        vertices[1] = new Vector2();
        vertices[2] = new Vector2();
        vertices[3] = new Vector2();

        resize(halfsize);
    }

    public void resize(Vector2 halfsize) {
        this.vertices[0].x = -1 * halfsize.x;
        this.vertices[0].y = -1 * halfsize.y;

        this.vertices[1].x = -1 * halfsize.x;
        this.vertices[1].y = 1 * halfsize.y;

        this.vertices[2].x = 1 * halfsize.x;
        this.vertices[2].y = 1 * halfsize.y;

        this.vertices[3].x = 1 * halfsize.x;
        this.vertices[3].y = -1 * halfsize.y;

        this.halfsize = halfsize;
    }

    @Override
    public Vector2[] getVertices() {
        return vertices;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getHalfsize() {
        return halfsize;
    }
}
