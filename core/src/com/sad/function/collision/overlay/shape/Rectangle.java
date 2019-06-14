package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

public class Rectangle extends Polygon {
    private Vector2 halfsize;
    private Vector2 position;
    private boolean centered;
    public Rectangle(Vector2 position, Vector2 halfsize, boolean centered) {
        super(calculateVertexes(halfsize, centered), centered);

        this.halfsize = halfsize;
        this.position = position;
        this.centered = centered;
    }

    private static Vector2[] calculateVertexes(Vector2 halfsize, boolean centered) {
        Vector2[] vertices = new Vector2[4];
        if (centered) {

            vertices[0] = new Vector2(-halfsize.x / 2, -halfsize.y / 2);
            vertices[1] = new Vector2(halfsize.x / 2, -halfsize.y / 2);
            vertices[2] = new Vector2(halfsize.x / 2, halfsize.y / 2);
            vertices[3] = new Vector2(-halfsize.x / 2, halfsize.y / 2);

        } else {

            vertices[0] = new Vector2(0, 0);
            vertices[1] = new Vector2(halfsize.x, 0);
            vertices[2] = new Vector2(halfsize.x, halfsize.y);
            vertices[3] = new Vector2(0, halfsize.y);

        }

        return vertices;
    }

    /**
     * Creates a new list of normal & normalized vectors for each axis.
     *
     * @return a normal and normalized array of vectors for use with the SAT algorithm.
     * <p>
     * TODO: Rectangles can be simplified, two of the axis are parallel so we don't need to check them.
     */
    @Override
    public Vector2[] getAxes() {
        Vector2[] axes = new Vector2[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            int j = i + 1 == nodes.length ? 0 : i + 1;

            axes[i] = new Vector2(nodes[i].x - nodes[j].x, nodes[i].y - nodes[j].y);
            axes[i].nor();
            axes[i] = normal(axes[i]);
        }
        return axes;
    }

    /**
     * Returns the list of vertices for this rectangle.
     * @return this rectangles vertices.
     */
    public Vector2[] getVertices() { return nodes; }

    @Override
    public int getNumberOfVertices() {
        return nodes.length;
    }

    public float getHalfsizeWidth() {
        return halfsize.x;
    }

    public float getHalfsizeHeight() {
        return halfsize.y;
    }

    public boolean isCentered() {
        return centered;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
