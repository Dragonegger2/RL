package com.sad.function.collision.other;

import com.badlogic.gdx.math.Vector2;

public class Collision {
    public static boolean test(IShape shapeA, IShape shapeB) {
        CollisionInfo result1;

        if (shapeA instanceof Circle && shapeB instanceof Circle) {
//            return checkCircleCollision((Circle)shapeA, (Circle)shapeB, true);
        }

        if (shapeA instanceof IPolygon && shapeB instanceof IPolygon) {
            result1 = checkPolygonsForSAT((IPolygon) shapeA, (IPolygon) shapeB, false, false);
            if(result1 == null) return false;
            result1 = checkPolygonsForSAT((IPolygon) shapeB, (IPolygon) shapeA, true, false);
            if(result1 == null) return false;

            return true;
        }

        //check for circle polygon collision.
        return false;
    }


    /**
     * Check for overlaps between polygons
     *
     * @param polygonA      shapeA
     * @param polygonB      shapeB
     * @param flip          flip
     * @param doCalculation docalculation
     * @return Collision info for these two polygons.
     */
    private static CollisionInfo checkPolygonsForSAT(IPolygon polygonA, IPolygon polygonB, boolean flip, boolean doCalculation) {
        float min0, max0, min1, max1;
        Vector2 vAxis;
        float sOffset;
        Vector2 vOffset;
        float d0, d1;
        int i, j;
        float t;
        Vector2[] p1;
        Vector2[] p2;
        Vector2 ra;

        float shortestDist = Float.POSITIVE_INFINITY;
        float distMin,
                distMinAbs;

        CollisionInfo result = new CollisionInfo();

        result.shapeA = (flip) ? polygonB : polygonA;
        result.shapeB = (flip) ? polygonA : polygonB;
        result.shapeAContained = true;
        result.shapeBContained = true;

        p1 = polygonA.getVertices();
        p2 = polygonB.getVertices();

        //TODO add method to check for line segment checks.

        vOffset = new Vector2(polygonA.getPosition().x - polygonB.getPosition().x,
                polygonA.getPosition().y - polygonB.getPosition().y);

        for (i = 0; i < p1.length; i++) {
            vAxis = getAxisNormal(p1, i);

            min0 = vAxis.dot(p1[0]);
            max0 = min0;

            for (j = 1; j < p1.length; j++) {
                t = vAxis.dot(p1[j]);
                if (t < min0) min0 = t;
                if (t > max0) max0 = t;
            }

            //project polygon B
            min1 = vAxis.dot(p2[0]);
            max1 = min1;

            for(j = 1; j< p2.length; j++) {
                t = vAxis.dot(p2[j]);
                if(t < min1 ) min1 = t;
                if(t > max1 ) max1 = t;
            }

            //Shift polygon's projected points
            sOffset = vAxis.dot(vOffset);
            min0 += sOffset;
            max0 += sOffset;

            d0 = min0 - max1;
            d1 = min1 - max0;

            if(d0 > 0 || d1 > 0) {
                //Gap found;
                return null;
            }

            if(doCalculation) {
                if(!flip) {
                    if(max0 > max1 || min0 < min1) result.shapeAContained = false;
                    if(max1 > max0 || min1 < min0) result.shapeBContained = false;
                } else {
                    if (max0 < max1 || min0 > min1) result.shapeAContained = false;
                    if (max1 < max0 || min1 > min0) result.shapeBContained = false;
                }

                distMin = (max1 - min0) * -1;
                if(flip) distMin *= -1;

                distMinAbs = (distMin < 0) ? distMin * -1 : distMin;
                if(distMinAbs < shortestDist) {
                    result.distance = distMin;
                    result.pVector = vAxis;

                    shortestDist = distMinAbs;
                }
            }
        }

        return result;
    }

    private static Vector2 getAxisNormal(Vector2[] vertexArray, int pointIndex) {
        Vector2 pt1 = vertexArray[pointIndex];
        Vector2 pt2 = (pointIndex >= vertexArray.length - 1) ? vertexArray[0] : vertexArray[pointIndex + 1];

        Vector2 p = new Vector2(-(pt2.y - pt1.y), pt2.x - pt1.x);
        return p.nor();
    }
}
