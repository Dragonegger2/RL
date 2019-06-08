package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;

public class Rectangle extends Polygon {
    public Vector2 origin;
    public Vector2 halfsize;

    public Rectangle(Vector2 origin, Vector2 halfsize) {
        this.origin = origin;
        this.halfsize = halfsize;

        this.vertices = new Vector2[4];
        this.vertices[0] = new Vector2();
        this.vertices[1] = new Vector2();
        this.vertices[2] = new Vector2();
        this.vertices[3] = new Vector2();
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

    public Vector2 getLeft() {
        return origin.cpy().sub(halfsize.x, 0);
    }

    public Vector2 getRight() {
        return origin.cpy().add(halfsize.x, 0);     //origin + width/2
    }

    public Vector2 getTop() {
        return origin.cpy().add(0, halfsize.y);
    }

    public Vector2 getBottom() {
        return origin.cpy().sub(0, halfsize.y);
    }

    public Vector2 getTopLeft() {
        return getLeft().add(0, halfsize.y);
    }

    public Vector2 getTopRight() {
        return getRight().add(0, halfsize.y);
    }

    public Vector2 getBottomLeft() {
        return getLeft().sub(0, halfsize.y);
    }

    public Vector2 getBottomRight() {
        return getRight().sub(0, halfsize.y);
    }

    @Override
    public Vector2 getOrigin() {
        return origin;
    }
}
