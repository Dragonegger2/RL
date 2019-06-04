package com.sad.function.system.collision;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.system.collision.shapes.Shape;

public class Distance2D {
    public static class Math {
        public static float cross(Vector2 a, Vector2 b) {
            return a.x * b.y - a.y * b.x;
        }

        public static Vector2 cross(Vector2 a, float s) {
            return new Vector2(s * a.y, -s * a.x);
        }

        public static Vector2 cross(float s, Vector2 a) {
            return new Vector2(-s * a.y, s * a.x);
        }
    }

    public class Simplex {
        float m_divisor;
        int m_count;
        SimplexVertex vertexA,
                vertexB,
                vertexC;

        Vector2 getSearchDirection() {
            switch (m_count) {
                case 1:
                    return vertexA.point.cpy().scl(-1);
                case 2:
                    Vector2 edgeAB = vertexB.point.cpy().sub(vertexA.point);
                    float sgn = Math.cross(edgeAB, vertexA.point.cpy().scl(-1));
                    if (sgn > 0.0f) {
                        return Math.cross(1.0f, edgeAB);
                    } else {
                        return Math.cross(edgeAB, 1.0f);
                    }
                default:
                    return new Vector2(0f, 0f);
            }
        }

        Vector2 getClosestPoint() {
            switch (m_count) {
                case 1:
                    return vertexA.point;
                case 2:
                    float s = 1.0f / m_divisor;
                    return vertexA.point.cpy().scl(s * vertexA.u).add(vertexB.point.cpy().scl(s * vertexB.u));//return (s * vertexA.u) * vertexA.point + (s * vertexB.u) * vertexB.point;
                case 3:
                    return new Vector2(0f, 0f);
                default:
                    assert (false);
                    return new Vector2(0.0f, 0.0f);

            }
        }

        void GetWitnessPoints(Vector2 point1, Vector2 point2) {
            float factor = 1.0f / m_divisor;

            switch (m_count) {
                case 1:
                    point1.set(vertexA.point1);
                    point2.set(vertexA.point2);
                    break;
                case 2:
                    float s = 1.0f / m_divisor;
                    point1.set(vertexA.point1.cpy().scl(s * vertexA.u).add(vertexB.point1.cpy().scl(s * vertexB.u)));
                    point2.set(vertexA.point2.cpy().scl(s * vertexA.u).add(vertexB.point2.cpy().scl(s * vertexB.u)));

                    break;
                case 3:
                    s = 1.0f / m_divisor;
                    //TODO Verify this math...
                    point1.set(vertexA.point1.cpy().scl(s * vertexA.u).add(vertexB.point1.cpy().scl(s * vertexB.u)).add(vertexC.point1.cpy().scl(s * vertexC.u)));
                    point2.set(point1);
                    break;
                default:
                    assert (false);
                    break;

            }
        }

        void solve2(Vector2 Q) {
            Vector2 A = vertexA.point.cpy();
            Vector2 B = vertexB.point.cpy();

            // Compute the barycentric coordinates (pre-division).
            float u = Q.cpy().sub(B).crs(A.cpy().sub(B));
            float v = Q.cpy().sub(A).crs(B.cpy().sub(A));

            // Region A
            if (v <= 0.0f) {
                vertexA.u = 1.0f;
                m_divisor = 1.0f;
                m_count = 1;
                return;
            } else if (u <= 0.0f) {
                vertexA = vertexB;
                vertexA.u = 1.0f;
                m_divisor = 1.0f;
                m_count = 1;
            } else {
                vertexA.u = u;
                vertexB.u = v;

                Vector2 e = B.cpy().sub(A);
                m_divisor = e.dot(e);
                m_count = 2;
            }
        }

        void solve3(Vector2 Q) {
            Vector2 A = vertexA.point.cpy();
            Vector2 B = vertexB.point.cpy();
            Vector2 C = vertexC.point.cpy();

            //Compute edge barycentric coordinates (pre-division).
            float uAB = Q.cpy().sub(B).dot(A.cpy().sub(B));
            float vAB = Q.cpy().sub(A).dot(B.cpy().sub(A));

            float uBC = Q.cpy().sub(C).dot(B.cpy().sub(C));
            float vBC = Q.cpy().sub(B).dot(C.cpy().sub(B));

            float uCA = Q.cpy().sub(A).crs(C.cpy().sub(A));
            float vCA = Q.cpy().sub(C).crs(A.cpy().sub(C));


            //Region A
            if(vAB <= 0.0f && uCA <= 0.0f) {
                vertexA.u = 1.0f;
                m_divisor = 1.0f;
                m_count = 1;
                return;
            }
            //Region B
            if (uAB <= 0.0f && vBC <= 0.0f)
            {
                vertexA = vertexB;
                vertexA.u = 1.0f;
                m_divisor = 1.0f;
                m_count = 1;
                return;
            }
            // Region C
            if (uBC <= 0.0f && vCA <= 0.0f)
            {
                vertexA = vertexC;
                vertexA.u = 1.0f;
                m_divisor = 1.0f;
                m_count = 1;
                return;
            }

            float area = B.cpy().sub(A).dot(C.cpy().sub(A));

            float uABC = B.cpy().sub(Q).dot(C.cpy().sub(Q));
            float vABC = C.cpy().sub(Q).dot(A.cpy().sub(Q));
            float wABC = A.cpy().sub(Q).dot(B.cpy().sub(Q));

            //Region AB
            if(uAB > 0.0f && vAB > 0.0f && wABC * area <= 0.0f) {
                vertexA.u = uAB;
                vertexB.u = vAB;
                Vector2 e = B.cpy().sub(A);
                m_divisor = e.dot(e);
                m_count = 2;
                return;
            }

            // Region BC
            if (uBC > 0.0f && vBC > 0.0f && uABC * area <= 0.0f)
            {
                vertexA = vertexB;
                vertexB = vertexC;

                vertexA.u = uBC;
                vertexB.u = vBC;
                Vector2 e = C.cpy().sub(B);
                m_divisor = e.dot(e);
                m_count = 2;

                return;
            }
            //Can combine the two sections of code.

            // Region CA
            if (uCA > 0.0f && vCA > 0.0f && vABC * area <= 0.0f)
            {
                vertexB = vertexA;
                vertexA = vertexC;

                vertexA.u = uCA;
                vertexB.u = vCA;
                Vector2 e = A.cpy().sub(C);
                m_divisor = e.dot(e);
                m_count = 2;
                return;
            }

            // Region ABC
            // The triangle area is guaranteed to be non-zero.
            assert(uABC > 0.0f && vABC > 0.0f && wABC > 0.0f);
            vertexA.u = uABC;
            vertexB.u = vABC;
            vertexC.u = wABC;
            m_divisor = area;
            m_count = 3;
        }
    }

    public class SimplexVertex {
        Vector2 point1; //Support point in polygon 1
        Vector2 point2; //Support point in polygon 2
        Vector2 point;  //Point2 - point1
        float u; //unnormalized barycentric coordinate for the closest point.
        int index1;     //point1 index;
        int index2;     //point2 index;
    }

    public class Output {
        private static final int MAX_SIMPLICES = 20;

        Vector2 point1; //Closest point on polygon 1
        Vector2 point2; //CLosest point on polygon 2
        float distance;
        int iterations; //number of GJK iterations used to solve for this.

        Simplex[] simplices = new Simplex[MAX_SIMPLICES];
        int simplexCount;
    }

    public Output distance2D(Shape a, Shape b, Vector2 transform1, Vector2 transform2) {

        Simplex simplex = new Simplex();
        simplex.vertexA.index1 = 0;
        simplex.vertexA.index2 = 0;

        Output output = new Output();

        //Calculate d = a.origin - b.origin
        Vector2 d = a.getOrigin().cpy().sub(b.getOrigin().cpy());

        Vector2 aSupp = a.support(d);
        Vector2 bSupp = b.support(d);


        int iter = 0,
            k_maxIters = 20;

        while(iter < k_maxIters) {
            switch (simplex.m_count) {
                case 1:
                    break;
                case 2:
                    simplex.solve2(new Vector2(0,0));
                    break;
                case 3:
                    simplex.solve3(new Vector2(0,0));
                    break;
                default:
                    assert(false);
            }

            //TODO not strictly needed, it's used in the example code for visualization.
            output.simplices[output.simplexCount++] = simplex;

            if(simplex.m_count == 3) {
                return output;
            }

            d = simplex.getSearchDirection();

            if(d.dot(d) == 0) {
                return output;
            }


        }

        //TODO: Figure out how to make this sue the support functions from the shape classes.
        return null;
    }
}
