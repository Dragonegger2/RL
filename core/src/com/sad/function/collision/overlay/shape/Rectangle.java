package com.sad.function.collision.overlay.shape;

import com.badlogic.gdx.math.Vector2;

public class Rectangle extends Polygon {
    private Vector2 halfsize;
    private Vector2 origin;
    private boolean centered;
    public Rectangle(Vector2 origin, Vector2 halfsize, boolean centered) {
        super(calculateVertexes(halfsize, centered), centered);

        this.halfsize = halfsize;
        this.origin = origin;
        this.centered = centered;
    }

    private static Vector2[] calculateVertexes(Vector2 halfsize, boolean centered) {
        Vector2[] vertices = new Vector2[4];
        if (centered) {
            vertices[0] = new Vector2(-halfsize.x, -halfsize.y);
            vertices[1] = new Vector2(halfsize.x, -halfsize.y);
            vertices[2] = new Vector2(halfsize.x, halfsize.y);
            vertices[3] = new Vector2(-halfsize.x, halfsize.y);
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
    public Vector2[] getAxes(Transform transform) {
        Vector2[] axes = new Vector2[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            int j = i + 1 == vertices.length ? 0 : i + 1;

            axes[i] = new Vector2(vertices[i].x + transform.x - vertices[j].x + transform.x ,
                    vertices[i].y + transform.y - vertices[j].y + transform.y);

            axes[i].nor();
            axes[i] = normal(axes[i]);
        }
        return axes;
    }

    /**
     * Returns the list of vertices for this rectangle.
     * @return this rectangles vertices.
     */
    public Vector2[] getRawVertices() { return vertices; }

    public Vector2[] getTransformedVertices(Transform t) {
        Vector2[] verts = new Vector2[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            verts[i] = vertices[i].cpy().add(t.x, t.y);
        }

        return verts;
    }

    @Override
    public int getNumberOfVertices() {
        return vertices.length;
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

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }
}
