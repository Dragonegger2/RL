package com.sad.function.system.cd.shapes;

import com.badlogic.gdx.math.Vector2;

public abstract class Shape {
    protected Vector2[] vertices;

    /**
     The origin / centre of the bodyShape.
     */
    public abstract Vector2 getOrigin();

    public Vector2[] getVertices() { return vertices; }
//    public abstract Vector2 support(Vector2 direction);
//    /**
//     Given a direction in global coordinates, return the vertex (in global coordinates)
//     that is the furthest in that direction
//     @param direction
//     @return Vec2
//     */
//    public Vector2 support(Vector2 direction) {
//        Vector2 farthestPoint = vertices.get(0);
//
//        for (Vector2 vertex : vertices) {
//            float a = direction.dot(vertex);
//            float b = direction.dot(farthestPoint);
//
//            if (a > b) {
//                farthestPoint = vertex;
//            }
//        }
//
//        return farthestPoint;
//    }

//    public List<Vector2> getVertices() {
//        return vertices;
//    }

    public Vector2 support(Vector2 direction) {
        Vector2 _origin = new Vector2(getOrigin().x, getOrigin().y);
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