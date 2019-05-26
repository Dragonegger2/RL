package com.sad.function.system.collision.headbutt.twod.shape;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.collision.headbutt.twod.Shape;

import java.util.ArrayList;
import java.util.List;

public class Polygon extends Shape {
    private List<Vector2> vertices;

    public Polygon(Vector2 origin, ArrayList<Vector2> vertices) {
        this._origin = origin;
        this.vertices = vertices;
    }


    @SuppressWarnings("Duplicates")
    @Override
    public Vector2 support(Vector2 direction) {
        float furthestDistance = Float.MIN_VALUE;
        Vector2 furthestVertex = new Vector2();

        Vector2 vo = new Vector2();
        for(Vector2 v : vertices) {
            vo.set(v);//.add(_origin); we're not going to offset by origin, fuck that.
            float distance = vo.dot(direction);
            if(distance > furthestDistance) {
                furthestDistance = distance;
                furthestVertex.set(vo);
            }
        }

        return furthestVertex;
    }
}
