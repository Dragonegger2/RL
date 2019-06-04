package com.sad.function.system;

import com.badlogic.gdx.math.Vector2;

public class Collision {
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
        public  float divisor = 1;

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

                    if(sgn > 0) {
                        return perpendicular(ab);
                    }
                    return rperp(ab);
            }

            return new Vector2();
        }

        public Vector2 getClosestPOint() {
            switch (this.count) {
                case 1:
                    return this.verts[0].p;
                case 2:
                    return 
            }
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
}
