package com.sad.function.system.collision.shapes;

import com.badlogic.gdx.math.Vector2;

public class Rectangle extends Shape {
    private Vector2[] vertices;

    public Rectangle(Vector2 origin, Vector2 halfsize) {
        this._origin = origin;
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
        this.vertices[1].y =  1 * halfsize.y;

        this.vertices[2].x =  1 * halfsize.x;
        this.vertices[2].y =  1 * halfsize.y;

        this.vertices[3].x =  1 * halfsize.x;
        this.vertices[3].y = -1 * halfsize.y;
    }

    @Override
    public Vector2 support(Vector2 direction) {
        Vector2 furthestVertex = vertices[0].cpy().add(_origin);

        Vector2 vo = new Vector2();
        for(Vector2 v : vertices) {

            vo.set(v).add(_origin);
             float a = direction.dot(vo);
            float b = direction.dot(furthestVertex);

            if(a > b) {
                furthestVertex.set(vo);
            }
        }

        return furthestVertex;
    }
}