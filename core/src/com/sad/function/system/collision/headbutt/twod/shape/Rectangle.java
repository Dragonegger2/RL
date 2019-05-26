package com.sad.function.system.collision.headbutt.twod.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.collision.headbutt.twod.Shape;

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

        this.vertices[1].x =  1 * halfsize.x;
        this.vertices[1].y = -1 * halfsize.y;

        this.vertices[2].x =  1 * halfsize.x;
        this.vertices[2].y =  1 * halfsize.y;

        this.vertices[3].x = -1 * halfsize.x;
        this.vertices[3].y =  1 * halfsize.y;
    }

    @Override
    public Vector2 support(Vector2 direction) {
        float furthestDistance = Float.MIN_VALUE;
        Vector2 furthestVertex = new Vector2();

        Vector2 vo = new Vector2();
        for(Vector2 v : vertices) {
            vo.set(v).add(_origin);
            float distance = vo.dot(direction);
            if(distance > furthestDistance) {
                furthestDistance = distance;
                furthestVertex.set(vo);
            }
        }

        return furthestVertex;
    }
}
