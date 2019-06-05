package com.sad.function.system;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Collision {
    /**
     * Calculates the support point (furthest point in a given direction d) of the polygon.
     *
     * @param polygon shape to search in.
     * @param d       direction to search in.
     * @return index of the vertices
     */
    public int supportPoint(Shape polygon, Vector2 d) {
        int bestIndex = 0;
        float bestValue = polygon.verts[0].dot(d);

        for (int i = 1; i < polygon.verts.length; i++) {
            float value = polygon.verts[i].dot(d);
            if (value > bestValue) {
                bestIndex = i;
                bestValue = value;
            }
        }

        return bestIndex;
    }

    private static final Vector2 zero = new Vector2(0,0);

    public List<Simplex> doGJK(Shape polygon1, Transform xf1, Shape polygon2, Transform xf2) {
        List<Simplex> simplexHistory = new ArrayList<>();

        Simplex simplex = new Simplex();
        simplex.count = 1;
        SimplexVertex v0 = simplex.verts[0];

        v0.index1 = supportPoint(polygon1, xf1.unrotate(new Vector2(-1, 0)));
        v0.index2 = supportPoint(polygon2, xf2.unrotate(new Vector2(1, 0)));

        Vector2 localPoint1 = polygon1.verts[v0.index1];
        Vector2 localPoint2 = polygon1.verts[v0.index2];
        v0.p1 = xf1.transform(localPoint1);
        v0.p2 = xf2.transform(localPoint2);
        v0.u = 1;

        int[] save1 = new int[3];
        int[] save2 = new int[3];
        int saveCount;

        SimplexVertex[] v = simplex.verts;
        int max_iters = 20;
        for (int iter = 0; iter < max_iters; iter++) {
            saveCount = simplex.count;
            for (int i = 0; i < saveCount; i++) {
                save1[i] = v[i].index1;
                save2[i] = v[i].index2;
            }

            switch (simplex.count) {
                case 1:
                    break;
                case 2:
                    simplex.solve2(zero);
                    break;
                case 3:
                    simplex.solve3(zero);
                    break;
                default:
                    assert(false);
            }

            Simplex record = new Simplex();
            record.copy(simplex);
            simplexHistory.add(record);

            if(simplex.count == 3) {
                break;
            }

            Vector2 d = simplex.getSearchDirection();

            if(d.dot(d) == 0) {
                break;
            }

            SimplexVertex new_v = v[simplex.count];
            new_v.index1 = supportPoint(polygon1, xf1.unrotate(d.cpy().scl(-1)));
            new_v.p1 = xf1.transform(polygon1.verts[new_v.index1]);
            new_v.index2 = supportPoint(polygon2, xf2.unrotate(d));
            new_v.p2 = xf2.transform(polygon2.verts[new_v.index2]);
            new_v.p = new_v.p2.cpy().sub(new_v.p1);

            boolean duplicate = false;

            for(int i = 0; i < saveCount; i++) {
                if(new_v.index1 == save1[i] && new_v.index2 == save2[i]) {
                    duplicate = true;
                    break;
                }
            }

            if(duplicate) {
                break;
            }

            simplex.count++;

        }

        return simplexHistory;
    }

    public class Transform {
        public Vector2 t;
        public float c;
        public float s;

        public Transform(Vector2 pos, float angle) {
            this.t = pos.cpy();
            this.c = (float) Math.cos((double) angle);
            this.s = (float) Math.sin((double) angle);
        }

        public Transform setRotation(float angle) {
            this.c = (float) Math.cos((double) angle);
            this.s = (float) Math.sin((double) angle);

            return this;
        }

        public Transform setPosition(Vector2 position) {
            this.t.set(position);

            return this;
        }

        public Vector2 rotate(Vector2 v) {
            return new Vector2(v.x * this.c - v.y * this.s, v.x * this.s + v.y * this.c);
        }

        public Vector2 unrotate(Vector2 v) {
            return new Vector2(v.x * this.c + v.y * this.s, -v.x * this.s + v.y * this.c);
        }

        public Vector2 transform(Vector2 v) {
            return new Vector2(v.x * this.c - v.y * this.s + this.t.x, v.x * this.s + v.y * this.c + t.y);
        }

        public Vector2 untransform(Vector2 v) {
            float px = v.x - t.x;
            float py = v.y - t.y;

            return new Vector2(px * this.c + py * this.s, -px * this.s + py * this.c);
        }
    }

    public class SimplexVertex {
        public Vector2 p1;
        public Vector2 p2;
        public Vector2 p;
        public float u;
        public int index1;
        public int index2;

        public SimplexVertex() {
            p1 = new Vector2();
            p2 = new Vector2();
            p = new Vector2();
        }

        public void copy(SimplexVertex v) {
            this.p1.set(v.p1);
            this.p2.set(v.p2);
            this.p.set(v.p);
            this.u = v.u;
            this.index1 = v.index1;
            this.index2 = v.index2;
        }
    }

    public class Simplex {
        public SimplexVertex[] verts = new SimplexVertex[3];
        public int count = 0;
        public float divisor = 1;

        public Simplex() {
            this.verts[0] = new SimplexVertex();
            this.verts[1] = new SimplexVertex();
            this.verts[2] = new SimplexVertex();
        }

        public void copy(Simplex s) {
            this.verts[0].copy(s.verts[0]);
            this.verts[1].copy(s.verts[1]);
            this.verts[2].copy(s.verts[2]);
            this.count = s.count;
            this.divisor = s.divisor;
        }

        public Vector2 getSearchDirection() {
            switch (this.count) {
                case 1:
                    return this.verts[0].p.cpy().scl(-1);
                case 2:
                    Vector2 ab = sub(verts[1].p, verts[0].p);
                    float sgn = verts[0].p.crs(ab);

                    if (sgn > 0) {
                        return perpendicular(ab);
                    }
                    return rperp(ab);
            }

            return new Vector2();
        }

        public Vector2 getClosestPoint() {
            switch (this.count) {
                case 1:
                    return this.verts[0].p;
                case 2:
                    return verts[1].p.cpy().lerp(this.verts[0].p, this.verts[1].u / this.divisor);
                case 3:
                    float s = 1f / this.divisor;
                    Vector2 p = this.verts[0].p.cpy().scl(this.verts[0].u * s);
                    p.add(p.cpy().scl(this.verts[1].u * s));
                    p.add(p.cpy().scl(this.verts[2].u * s));

                    return p;
            }

            return new Vector2();
        }

        public Witness getWitnessPOints() {
            Witness w = new Witness();

            float s;
            switch (this.count) {
                case 1:
                    w.p1.set(this.verts[0].p1);
                    w.p2.set(this.verts[0].p2);
                    return w;
                case 2:
                    s = this.verts[1].u / this.divisor;
                    w.p1 = this.verts[1].p1.lerp(this.verts[0].p, s);
                    w.p2 = this.verts[1].p2.lerp(this.verts[0].p2, s);
                    return w;
                case 3:
                    s = 1 / this.divisor;
                    w.p1 = this.verts[0].p1.cpy().scl(this.verts[0].u * s);
                    w.p1.mulAdd(w.p1, this.verts[1].u * s);
                    w.p1.mulAdd(w.p1, this.verts[2].u * s);

                    return w;
            }

            return null;
        }

        public void solve2(Vector2 q) {
            Vector2 a = this.verts[0].p.cpy();
            Vector2 b = this.verts[1].p.cpy();

            Vector2 ab = b.cpy().sub(a);

            Vector2 aq = q.cpy().sub(a);
            float v = aq.dot(ab);

            if (v <= 0) {
                this.count = 1;
                this.verts[0].u = 1;
                this.divisor = 1;
                return;
            }

            Vector2 bq = q.cpy().sub(b);
            float u = -bq.dot(ab);
            if (u <= 0) {
                this.count = 1;
                this.verts[0].copy(this.verts[1]);
                this.verts[0].u = 1;
                this.divisor = 1;
                return;
            }

            this.count = 2;
            this.verts[0].u = u;
            this.verts[1].u = v;
            this.divisor = ab.len2();
        }

        public void solve3(Vector2 q) {
            Vector2 a = this.verts[0].p;
            Vector2 b = this.verts[1].p;
            Vector2 c = this.verts[2].p;

            Vector2 ab = b.cpy().sub(a);
            Vector2 bc = c.cpy().sub(b);
            Vector2 ca = a.cpy().sub(c);

            Vector2 aq = q.cpy().sub(a);
            float vab = aq.dot(ab);
            float uca = -aq.dot(ca);
            if (vab <= 0 && uca <= 0) {
                this.count = 1;
                this.verts[0].u = 1;
                this.divisor = 1;
                return;
            }

            Vector2 bq = q.cpy().sub(b);
            float vbc = bq.dot(bc);
            float uab = -bq.dot(ab);
            if (vbc <= 0 && uab <= 0) {
                this.count = 1;
                this.verts[0].copy(this.verts[1]);
                this.verts[0].u = 1;
                this.divisor = 1;

                return;
            }

            Vector2 cq = q.cpy().sub(c);
            float vca = cq.dot(ca);
            float ubc = -cq.dot(bc);
            if (vca <= 0 && ubc <= 0) {
                this.count = 1;
                this.verts[0].copy(this.verts[2]);
                this.verts[0].u = 1;
                this.divisor = 1;
                return;
            }

            // Compute signed triangle area x 2.
            float area = ab.crs(ca);

            // Region AB
            float wabc = aq.crs(bq);
            if (uab > 0 && vab > 0 && (wabc * area < 0 || area == 0)) {
                this.count = 2;
                this.verts[0].u = uab;
                this.verts[1].u = vab;
                this.divisor = ab.len2();
                return;
            }

            // Region BC
            float uabc = bq.crs(cq);
            if (ubc > 0 && vbc > 0 && uabc * area < 0) {
                this.count = 2;
                this.verts[0].copy(this.verts[1]);
                this.verts[1].copy(this.verts[2]);
                this.verts[0].u = ubc;
                this.verts[1].u = vbc;
                this.divisor = bc.len2();
                return;
            }

            // Region CA
            float vabc = cq.crs(aq);
            if (uca > 0 && vca > 0 && vabc * area < 0) {
                this.count = 2;
                this.verts[1].copy(this.verts[0]);
                this.verts[0].copy(this.verts[2]);
                this.verts[0].u = uca;
                this.verts[1].u = vca;
                this.divisor = ca.len2();
                return;
            }

            // The triangle area is guaranteed to be non-zero.
            //assert(uabc > 0 && vabc > 0 && wabc > 0);

            // Region ABC
            this.count = 3;
            this.verts[0].u = uabc;
            this.verts[1].u = vabc;
            this.verts[2].u = wabc;
            this.divisor = area;
        }


        private Vector2 perpendicular(Vector2 v) {
            return new Vector2(-v.y, v.x);
        }

        private Vector2 rperp(Vector2 v) {
            return new Vector2(v.y, -v.x);
        }

        private Vector2 sub(Vector2 a, Vector2 b) {
            return new Vector2(a.x - b.x, a.y - b.y);
        }

    }

    public class Witness {
        public Vector2 p1;
        public Vector2 p2;

        public Witness() {
            p1 = new Vector2();
            p2 = new Vector2();
        }
    }

    public class Polytope {

    }

    public abstract class Shape {
        public Vector2[] verts;
    }
}
