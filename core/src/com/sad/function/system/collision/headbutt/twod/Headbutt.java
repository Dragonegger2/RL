package com.sad.function.system.collision.headbutt.twod;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

enum EvolveResult {
    NoIntersection,
    FoundIntersection,
    StillEvolving
}

enum PolygonWinding {
    Clockwise,
    CounterClockwise
}

class Edge {
    public double distance;
    public Vector2 normal;
    public int index;

    public Edge() {

    }
    public Edge(double distance, Vector2 normal, int index) {
        this.distance = distance;
        this.normal = normal;
        this.index = index;
    }
}

public class Headbutt {
    /**
     * The maximum number of simplex evolution iterations before we accept the
     * given simplex. For non-curvy shapes, this can be low. Curvy shapes potentially
     * require higher numbers, but this can introduce significant slow-downs at
     * the gain of not much accuracy.
     */
    public int maxIterations = 20;
    private ArrayList<Vector2> vertices;
    private Vector2 direction;
    private Shape shapeA;
    private Shape shapeB;

    /**
     * Create a new Headbutt instance. Headbutt needs to be instantiated because
     * internally it stores state. This may change in the future.
     */
    public Headbutt() {
        vertices = new ArrayList<>();
    }

    private Vector2 calculateSupport(Vector2 direction) {
        Vector2 oppositeDirection = new Vector2(direction).scl(-1);
        Vector2 newVertex = shapeA.support(direction);
        newVertex.sub(shapeB.support(oppositeDirection));

        return newVertex;
    }

    private boolean addSupport(Vector2 direction) {
        Vector2 newVertex = calculateSupport(direction);
        vertices.add(newVertex);
        return direction.dot(newVertex) >= 0; //Vec2.dot(direction, newVertex) >= 0;
    }

    public Vector2 tripleProduct(Vector2 a, Vector2 b, Vector2 c) {
        Vector3 A = new Vector3(a.x, a.y, 0);
        Vector3 B = new Vector3(b.x, b.y, 0);
        Vector3 C = new Vector3(c.x, c.y, 0);

        Vector3 calc = new Vector3(A).crs(B);
        calc.crs(C);

        return new Vector2(calc.x, calc.y);
    }

    private boolean evolveSimplex() {
        switch (vertices.size()) {
            case 0: {
                direction = new Vector2(shapeB.getOrigin()).sub(shapeA.getOrigin());
                break;
            }
            case 1: {
                // flip the direction
                direction = direction.scl(-1);
                break;
            }
            case 2: {
                // line ab is the line formed by the first two vertices
//                Vector2 ab = vertices.get(1) - vertices.get(0);
                Vector2 ab = new Vector2(vertices.get(1)).sub(vertices.get(0));
                // line a0 is the line from the first vertex to the origin
                Vector2 a0 = new Vector2(vertices.get(0)).scl(-1);

                // use the triple-cross-product to calculate a direction perpendicular
                // to line ab in the direction of the origin
                direction = tripleProduct(ab, a0, ab);
                break;
            }
            case 3: {
                // calculate if the simplex contains the origin
                /*var a:Vec2 = vertices[2];
                var b:Vec2 = vertices[1];
                var c:Vec2 = vertices[0];*/
//
//                Vector2 c0 = new Vector2(vertices.get(2)).scl(-1);
//                Vector2 bc = new Vector2(vertices.get(1)).sub(vertices.get(2));
//                Vector2 ca = new Vector2(vertices.get(0)).sub(vertices.get(2));
//
//                Vector2 bcNorm = tripleProduct(ca, bc, bc);
//                Vector2 caNorm = tripleProduct(bc, ca, ca);
//
//                if (bcNorm.dot(c0) > 0) {
//                    // the origin is outside line bc
//                    // get rid of a and add a new support in the direction of bcNorm
////                    vertices.remove(vertices[0]);
//                    vertices.remove(0);
//                    direction = bcNorm;
//                } else if (caNorm.dot(c0) > 0) {
//                    // the origin is outside line ca
//                    // get rid of b and add a new support in the direction of caNorm
////                    vertices.remove(vertices[1]);
//                    vertices.remove(1);
//                    direction = caNorm;
//                } else {
//                    // the origin is inside both ab and ac,
//                    // so it must be inside the triangle!
//                    return EvolveResult.FoundIntersection;
//                }
                Vector2 first = new Vector2(vertices.get(2));
                Vector2 firstFromOrigin = new Vector2(first).scl(-1);
                Vector2 second = new Vector2(vertices.get(1));
                Vector2 third = new Vector2(vertices.get(0));

                Vector2 firstSecond = new Vector2(second).sub(first);
                Vector2 firstThird = new Vector2(third).sub(first);

                Vector2 direction = new Vector2(first.y, -1 * firstSecond.x);
                if(third.dot(direction) > 0) {
                    direction.scl(-1);
                }

                if(direction.dot(firstFromOrigin) > 0) {
                    vertices.remove(0);
                    return false;
                }

                direction = new Vector2(firstThird.y, -1 * firstThird.x);

                if(direction.dot(firstFromOrigin) > 0){
                    vertices.remove(1);
                    return false;
                }
                return true;
            }
            default:
                throw new RuntimeException("Can\'t have simplex with ${vertices.length} verts!");
        }

        return addSupport(direction);
    }

    //Calculates a single point in the MinkowskiDifference.
    private Vector2 support(Shape a, Shape b, Vector2 direction) {
        Vector2 first = a.support(direction);
        Vector2 newDirection = new Vector2(direction).scl(-1);
        Vector2 second = b.support(newDirection);

        return first.sub(second);
    }

//    public boolean GJK(Shape a, Shape b) {
//        Vector2 direction = new Vector2(1, 0);
//        ArrayList<Vector2> simplex = new ArrayList<>();
//        simplex.add(support(a, b, direction));
//
//        Vector2 newDirection = new Vector2(direction).scl(-1);
//
//        int accumulator = 0;
//        while (accumulator < maxIterations) {
//            simplex.add(support(a, b, newDirection));
//
//            Vector2 calc = new Vector2(simplex.get(simplex.size()-1));
//
//            if (calc.dot(newDirection) <= 0) {
//                return false;
//            } else {
////                if(checkContainsOrigin(newDirection, simplex)) {
////                    return true;
////                }
//            }
//
//        }
//
//    }

    public boolean intersects(Shape a, Shape b) {
        Vector2 direction = new Vector2(1,0);
        List<Vector2> simplex = new ArrayList<>();
        simplex.add(support(a, b, direction));

        direction = direction.set(simplex.get(0)).scl(-1);

        int accumulator = 0;
        while(accumulator < maxIterations) {
            simplex.add(support(a, b, direction));
            if(!isSameDirection(direction, simplex.get(0))) {
                return false;
            }

            if(processSimplex(simplex, direction)) {
                return true;
            }

            accumulator++;
        }
        return false;
    }

    boolean processSimplex(List<Vector2> simplex, Vector2 direction) {
        if(simplex.size() == 2) //1 -simple
            if(isSameDirection(simplex.get(0).cpy().scl(-1), simplex.get(1).cpy().sub(simplex.get(0)) )) {
                direction = perpendicular(simplex.get(1).cpy().sub(simplex.get(0)));
                direction.scl(-simplex.get(0).cpy().dot(direction));
            } else {
                direction = simplex.get(0).scl(-1);
                //Remove b simple ie 1.
                simplex.remove(1);
            }
        else { //2-simplex
            Vector2 AB = simplex.get(1).cpy().sub(simplex.get(0));
            Vector2 AC = simplex.get(2).cpy().sub(simplex.get(0));
            Vector2 A0 = simplex.get(0).cpy().scl(-1);

            Vector2 ACB = perpendicular(AB);
            ACB = ACB.scl(ACB.dot(AC.cpy().scl(-1)));

            if(isSameDirection(ACB, A0)) {
                if(isSameDirection(AB, A0)) { //REGION 4
                    direction.set(A0);
                    //TODO: Double check this. Remove the b & c simplexs?
                    simplex.remove(1);
                    simplex.remove(2);
                    return false;
                } else {            //REGION 6
                    direction.set(A0);
                    //Remove the b simplex. IE 1.
                    simplex.remove(1);
                    return false;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates a vector that is perpendicular to the provided vector.
     * @param v
     * @return the perpendicular vector.
     */
    private Vector2 perpendicular(Vector2 v) {
        return new Vector2(-v.y, v.x);
    }
    private boolean isSameDirection(Vector2 v1, Vector2 v2) {
        return v1.dot(v2) > 0;
    }

    /**
     * Given two convex shapes, test whether they overlap or not
     *
     * @param shapeA
     * @param shapeB
     * @return Bool
     */
    public boolean test(Shape shapeA, Shape shapeB) {
        // reset everything
        this.vertices = new ArrayList<>();
        this.shapeA = shapeA;
        this.shapeB = shapeB;

        // do the actual test
        boolean result = false;
        int iterations = 0;
        while (iterations < maxIterations && !result) {
            result = evolveSimplex();
            iterations++;
        }
        return result;
    }

    private Edge findClosestEdge(PolygonWinding winding) {
        float closestDistance = Float.MAX_VALUE;
        Vector2 closestNormal = new Vector2();
        int closestIndex = 0;
        Vector2 line = new Vector2();
        for (int i = 0; i < vertices.size(); i++) {
            int j = i + 1;
            if (j >= vertices.size()) j = 0;

            line = vertices.get(j);
            line.sub(vertices.get(i));

            Vector2 norm;
            if (winding == PolygonWinding.Clockwise) {
                norm = new Vector2(line.y, -line.x);
            } else { //(winding == PolygonWinding.CounterClockwise) {
                norm = new Vector2(-line.y, line.x);
            }

            norm.nor();

            // calculate how far away the edge is from the origin
            float dist = norm.dot(vertices.get(i));
            if (dist < closestDistance) {
                closestDistance = dist;
                closestNormal = norm;
                closestIndex = j;
            }
        }

        return new Edge(closestDistance, closestNormal, closestIndex);
    }

//    /**
//     Given two shapes, test whether they overlap or not. If they don't, returns
//     `null`. If they do, calculates the penetration vector and returns it.
//     @param shapeA
//     @param shapeB
//     @return Null<Vec2>
//     */
//    public Vector2 intersect(Shape shapeA, Shape shapeB) {
//        // first, calculate the base simplex
//        if(!test(shapeA, shapeB)) {
//            // if we're not intersecting, return null
//            return null;
//        }
//
//        // calculate the winding of the existing simplex
//        var e0:Float = (vertices[1].x - vertices[0].x) * (vertices[1].y + vertices[0].y);
//        var e1:Float = (vertices[2].x - vertices[1].x) * (vertices[2].y + vertices[1].y);
//        var e2:Float = (vertices[0].x - vertices[2].x) * (vertices[0].y + vertices[2].y);
//        var winding:PolygonWinding =
//        if(e0 + e1 + e2 >= 0) PolygonWinding.Clockwise;
//        else PolygonWinding.CounterClockwise;
//
//        var intersection:Vec2 = new Vec2();
//        for(i in 0...32) {
//            var edge:Edge = findClosestEdge(winding);
//            var support:Vec2 = calculateSupport(edge.normal);
//            var distance:Float = support.dot(edge.normal);
//
//            intersection = edge.normal.copy(intersection);
//            intersection.multiplyScalar(distance, intersection);
//
//            if(Math.abs(distance - edge.distance) <= 0.000001) {
//                return intersection;
//            }
//            else {
//                vertices.insert(edge.index, support);
//            }
//        }
//
//        return intersection;
//    }
}