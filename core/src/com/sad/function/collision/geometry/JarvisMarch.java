package com.sad.function.collision.geometry;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jarvis's March algorithm.
 */
public class JarvisMarch {

    /**
     * Calculates the polar angle between the points.
     * @param p
     * @param q
     * @param r
     * @return
     */
    private static boolean CCW(Vector2 p, Vector2 q, Vector2 r) {
        float value = (q.y - p.y) * (r.x - q.x) - (q.x - p.x)*(r.y-q.y);

        if(value >=0) return false;
        return true;
    }

    /**
     * Need to provide a list of transformed vertices from two polygons. This algorithm could also be used to
     * gift-wrap a concave polygon.
     * @param points from two transformed polygons.
     * @return a convex hull calculated from the provided points.
     */
    public static List<Vector2> createConvexHull(List<Vector2> points) {
        List<Vector2> convexHull = new ArrayList<>();

        int n = points.size();

        if(n < 3) {
            return convexHull;
        }

        float[] next = new float[n];
        Arrays.fill(next, -1);

        //Find the leftmost point in the provided points.
        int leftMost = 0;
        for(int i = 0; i < n; i++) {
            if(points.get(i).x < points.get(leftMost).x) {
                leftMost = i;
            }
        }
        int p = leftMost,
            q;

        do{
            q = (p+1)%n;
            for(int i = 0; i< n; i++){
                if(CCW(points.get(p), points.get(i), points.get(q))) {
                    q = i;
                }
            }

            next[p] = q;
        } while(p != leftMost);

        //TODO: Add the points back in. Need to rewrite this.
        for(int i = 0; i < next.length; i++) {
            if(next[i] != -1)
                convexHull.add(points.get(i));
        }

        return convexHull;
    }
}
