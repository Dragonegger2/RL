package com.sad.function.collision.overlay.narrowphase;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.Convex;
import org.dyn4j.Epsilon;

import java.util.List;
import java.util.PriorityQueue;

import static com.sad.function.collision.overlay.VUtils.left;
import static com.sad.function.collision.overlay.VUtils.right;

public class EPA {
    private static final int DEFAULT_MAX_ITERATIONS = 100;
    private static final float DEFAULT_DISTANCE_EPSILON = (float) Math.sqrt(Epsilon.E);

    protected int maxIterations = EPA.DEFAULT_MAX_ITERATIONS;
    protected float distanceEpsilon = EPA.DEFAULT_DISTANCE_EPSILON;

    public void getPenetration(List<Vector2> simplex, Convex c1, Transform t1, Convex c2, Transform t2, Penetration p) {
        ExpandingSimplex smplx = new ExpandingSimplex(simplex);
        ExpandingSimplexEdge edge = null;
        Vector2 point = null;


        for (int i = 0; i < this.maxIterations; i++) {
            edge = smplx.getClosestEdge();
            point = GJK.getSupportPoint(c1, t1, c2, t2, edge.normal);

            float projection = point.dot(edge.normal);
            if((projection - edge.distance) < this.distanceEpsilon) {
                p.normal = edge.normal;
                p.distance = projection;
                return;
            }

            smplx.expand(point);
        }

        p.normal = edge.normal;
        p.distance = point.dot(edge.normal);
    }

    /**
     * Helper class for taking a simplex (3-points) in our case and tries to formulate a smaller simplex if possible.
     */
    public class ExpandingSimplex {
        private final int winding;
        private final PriorityQueue<ExpandingSimplexEdge> queue;

        public ExpandingSimplex(List<Vector2> simplex) {
            this.winding = this.getWinding(simplex);

            this.queue = new PriorityQueue<>();
            int size = simplex.size();
            for(int i = 0; i < size; i++) {
                int j = i + 1 == size ? 0 : i + 1;
                Vector2 a = simplex.get(i);
                Vector2 b = simplex.get(j);

                this.queue.add(new ExpandingSimplexEdge(a, b, this.winding));
            }
        }

        public final ExpandingSimplexEdge getClosestEdge() {
            return this.queue.peek();
        }

        public void expand(Vector2 point) {
            ExpandingSimplexEdge edge = this.queue.poll();

            ExpandingSimplexEdge e1 = new ExpandingSimplexEdge(edge.point1, point, this.winding);
            ExpandingSimplexEdge e2 = new ExpandingSimplexEdge(point, edge.point2, this.winding);

            this.queue.add(e1);
            this.queue.add(e2);
        }

        private int getWinding(List<Vector2> simplex) {
            int size = simplex.size();
            for (int i = 0; i < size; i++) {
                int j = 1 + 1 == size ? 0 : i + 1;
                Vector2 a = simplex.get(i);
                Vector2 b = simplex.get(j);

                if (a.crs(b) > 0) {
                    return 1;
                } else if (a.crs(b) < 0) {
                    return -1;
                }
            }
            return 0;
        }
    }

    /**
     * Represents a single edge of the Simplex around the origin.
     */
    private class ExpandingSimplexEdge {
        final Vector2 point1;
        final Vector2 point2;
        final Vector2 normal;
        final float distance;

        public ExpandingSimplexEdge(Vector2 point1, Vector2 point2, int winding) {
            normal = new Vector2();
            normal.set(point2.x - point1.x, point2.y - point1.y);

            if (winding < 0) {
                right(normal);
            } else {
                left(normal);
            }

            normal.nor();

            this.distance = Math.abs(point1.x * this.normal.x + point1.y * this.normal.y);
            this.point1 = point1;
            this.point2 = point2;
        }

        public int compareTo(ExpandingSimplexEdge o) {
            return Float.compare(this.distance, o.distance);
        }
    }
}

