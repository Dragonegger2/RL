package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

public class Polygon extends Shape {
    private Vector2[] nodes;

    public Polygon(Vector2[] vertexes) {
        nodes = new Vector2[vertexes.length];

        for(int i = 0; i < vertexes.length; i++) {
            nodes[i] = vertexes[i].cpy();
        }
    }
    @Override
    public Vector2 getVertex(int i, Vector2 axis) {
        return nodes[i];
    }

    @Override

    public Vector2[] getAxes() {
        Vector2[] axes = new Vector2[nodes.length];
        for(int i = 0; i < nodes.length; i++) {
            int j = i+1 == nodes.length ? 0 : i + 1;

            axes[i] = new Vector2(nodes[i].x - nodes[j].x, nodes[i].y - nodes[j].y);
            axes[i].nor();
            axes[i] = normal(axes[i]);
        }
        return axes;
    }

    @Override
    public int getNumberOfVertices() {
        return nodes.length;
    }
}
