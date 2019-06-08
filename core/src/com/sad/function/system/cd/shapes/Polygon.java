package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;

import java.util.ArrayList;

public class Polygon extends Shape {
    protected Vector2[] vertices;

    public Polygon(Translation translation, Vector2[] vertices) {
        super(translation);
        this.vertices = vertices;
    }

    public Vector2[] getVertices() {
        return vertices;
    }

    public void setVertices(Vector2[] vertices) {
        this.vertices = vertices;
    }

    @Override
    public Vector2 support(Vector2 direction) {
        Vector2 _origin = new Vector2(translation.x, translation.y);
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
