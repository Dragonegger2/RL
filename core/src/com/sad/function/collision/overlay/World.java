package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.broadphase.AbstractBroadphaseDetector;
import com.sad.function.collision.overlay.broadphase.BroadphasePair;
import com.sad.function.collision.overlay.broadphase.Sap;
import com.sad.function.collision.overlay.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.continuous.ConservativeAdvancement;
import com.sad.function.collision.overlay.data.*;
import com.sad.function.collision.overlay.filter.DetectBroadphaseFilter;
import com.sad.function.collision.overlay.filter.Filter;
import com.sad.function.collision.overlay.narrowphase.CollisionManifold;
import com.sad.function.collision.overlay.narrowphase.GJK;
import com.sad.function.collision.overlay.shape.Convex;

import java.util.ArrayList;
import java.util.List;

public class World {
    private static final float MAX_LINEAR_CORRECTION = 0.2f;
    private static final float MAX_LINEAR_TOLERANCE = 0.005f;
    private static final float DEFAULT_MAX_TRANSLATION = 2.0f;
    private static final float DEFAUL_MAX_ROTATION = (float)(0.5f * Math.PI);

    private final List<Body> bodies;
    private final List<Joint> joints;
    private final AbstractBroadphaseDetector<Body, BodyFixture> broadphase = new Sap<>();
    public float step = 0;
    protected BroadphaseFilter<Body, BodyFixture> detectBroadphaseFilter = new DetectBroadphaseFilter();
    private ConservativeAdvancement timeOfImpactSolver = new ConservativeAdvancement();
    private Vector2 gravity = new Vector2(0, -9.8f);
    private NarrowPhaseDetector narrowphase = new GJK();


    public World() {
        bodies = new ArrayList<>(65);
        joints = new ArrayList<>();
    }

    /**
     * Registers a body to be managed by the world.
     *
     * @param body to register.
     */
    public void addBody(Body body) {
        bodies.add(body);
        body.world = this;
        broadphase.add(body);
    }

    /**
     * @param body {@link Body}
     * @return if the bodies were removed.
     */
    public boolean removeBody(Body body) {
        broadphase.remove(body);
        return bodies.remove(body);
    }

    public void update(float delta) {
        this.step = delta;
        step();
        detect();
    }

    private void step() {
        updateBodies();
    }

    private void detect() {
        //DO TOI
//        timeOfImpactSolver.solve()
        for (int i = 0; i < bodies.size(); i++) {
            //
        }
        for (int i = 0; i < bodies.size(); i++) {
            broadphase.update(bodies.get(i));
        }
        List<BroadphasePair<Body, BodyFixture>> pairs = broadphase.detect(detectBroadphaseFilter);
        int pSize = pairs.size();

        ArrayList<CollisionManifold> manifolds = new ArrayList<>();

        for (int i = 0; i < pSize; i++) {
            BroadphasePair<Body, BodyFixture> pair = pairs.get(i);

            Body body1 = pair.getCollidable1();
            Body body2 = pair.getCollidable2();
            BodyFixture fixture1 = pair.getFixture1();
            BodyFixture fixture2 = pair.getFixture2();

            Transform transform1 = body1.getTransform();
            Transform transform2 = body2.getTransform();

            Convex convex2 = fixture2.getShape();
            Convex convex1 = fixture1.getShape();

            Penetration penetration = new Penetration();

            if (this.narrowphase.detect(convex1, transform1, convex2, transform2, penetration)) {
                if (penetration.getDepth() == 0.0f) {
                    continue;
                }

                manifolds.add(new CollisionManifold(penetration.normal.cpy(), penetration.distance, body1, fixture1, body2, fixture2));
            }

            /**
             * Handle all of my collisions.
             */
            for (CollisionManifold manifold : manifolds) {
                Body b = manifold.body1.isStatic() ? body2 : body1;

                b.translate(manifold.normal.scl(manifold.distance));

                //Cast a ray in the direction of the penetration (can be infinite I guess).
                //Wait, I've already got the collision information...

                //Create a method for ray detector.
                //Ray r = new Ray(b.getWorldCenter(), manifold.normal);
                //narrowphase.raycast(r, b.getRotationDiscRadius() + manifold.distance, )


                //I think this will kill my velocity if my collision happens. This will be useful.
                //Something seems to be causing a "slipping" to occur.
                if (Math.abs(manifold.normal.x) > Math.abs(manifold.normal.y)) {
                    b.getLinearVelocity().set(0, b.getLinearVelocity().y);
                } else {
                    b.getLinearVelocity().set(b.getLinearVelocity().x, 0);
                }
            }
        }
    }

    private void updateBodies() {
        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            body.transform0.set(body.getTransform());
            accumulateBodyVelocity(body, step);
        }

        for (int i = 0; i < bodies.size(); i++) {
            translateBodies(bodies.get(i));
        }
    }

    private void accumulateBodyVelocity(Body body, float delta) {
        if (!body.isDynamic()) return;

        body.accumulate(delta);
        body.velocity.x += (body.force.x * body.mass + gravity.x * body.gravityScale) * delta;
        body.velocity.y += (body.force.y * body.mass + gravity.y * body.gravityScale) * delta;

        float linear = 1.0f - delta * body.linearDamping;
        float angular = 1.0f - delta * body.angularDamping;

        linear = MathUtils.clamp(linear, 0.0f, 1.0f);
        angular = MathUtils.clamp(angular, 0.0f, 1.0f);

        body.velocity.x *= linear;
        body.velocity.y *= linear;
        body.angularVelocity *= angular;
    }

    private void translateBodies(Body body) {
        if (body.isStatic()) return;

        float translationX = body.velocity.x * step;
        float translationY = body.velocity.y * step;

        float rotation = body.angularVelocity * step;

        body.translate(translationX, translationY);
        body.rotateAboutCenter(rotation);
    }

    public List<Body> getBodies() {
        return bodies;
    }

    private void solveTOI() {
        //Listeners

        int size = bodies.size();
        for(int i = 0; i < size; i++) {
            Body body = bodies.get(i);

            if(!body.isBullet()) continue;//Skip if the body is a bullet.

            if(body.mass == -1) continue;   //We're only handling non-static bodies.
            
            solveTOI(body);
        }
    }

    private void solveTOI(Body body1) {
        int size = bodies.size();

        AABB aabb1 = body1.createSweptAABB();
        boolean bullet = body1.isBullet();

        float t1 = 0.0f;
        float t2 = 1.0f;

        TimeOfImpact minToi = null;
        Body minBody = null;

        for(int i = 0; i < size; i++) {
            Body body2 = bodies.get(i);

            if(body1 == body2) continue; //Skip if they are the same body.
            if(body2.isDynamic() && !bullet) continue; //skip other dynamic to dynamic collisions. We only do that in the event of "bullets"

            AABB aabb2 = body2.createSweptAABB();

            if(!aabb1.overlaps(aabb2)) continue;//Don't test if they are overlapping.

            TimeOfImpact toi = new TimeOfImpact();
            int fc1 = body1.getFixtureCount();
            int fc2 = body2.getFixtureCount();

            float dt = step; //TODO Make sure that we actually set the delta.

            Vector2 v1 = body1.getLinearVelocity().cpy().scl(dt);
            Vector2 v2 = body2.getLinearVelocity().cpy().scl(dt);
            float av1 = body1.getAngularVelocity() * dt;
            float av2 = body2.getAngularVelocity() * dt;

            Transform tx1 = body1.getInitialTransform();
            Transform tx2 = body2.getInitialTransform();

            for(int j = 0; j < fc1; j++) {
                BodyFixture f1 = body2.getFixture(j);

                if(f1.isSensor()) continue; //we skip sensors.

                for(int k = 0; k < fc2; k++) {
                    BodyFixture f2 = body2.getFixture(k);

                    if(f2.isSensor()) continue;

                    Filter filter1 = f1.getFilter();
                    Filter filter2 = f2.getFilter();

                    //Ensure the fixtures are actually allowed to collide.
                    if(!filter1.isAllowed(filter2)) {
                        continue;
                    }

                    Convex c1 = f1.getShape();
                    Convex c2 = f2.getShape();

                    if(timeOfImpactSolver.solve(c1, tx1, v1, av1, c2, tx2, v2, av2, t1, t2, toi)) {
                        float t = toi.getTime();

                        if(t < t2) {
                            boolean allow = true;
                            //Alert all listeners if we want to.

                            if(allow) {
                                t2 = t;
                                minToi = toi;
                                minBody = body2;
                            }
                        }


                    }

                }
            }


        }
        if(minToi != null) {
            float t = minToi.getTime();

            body1.transform0.lerp(body1.getTransform(), t, body1.getTransform());

            if(minBody.isDynamic()) {
                minBody.transform0.lerp(minBody.getTransform(), t, minBody.getTransform());
            }

            solve(body1, minBody, minToi);
        }
    }

    private void solve(Body body1, Body body2, TimeOfImpact timeOfImpact) {
        //Linear tolerance?
        //max linear correction
//        float linearTolerance = 1;//>

        Vector2 c1 = body1.getWorldCenter();
        Vector2 c2 = body2.getWorldCenter();

        //Get Mass...
        float mass1 = body1.mass;
        float mass2 = body2.mass;

        Separation separation = timeOfImpact.getSeparation();

        Vector2 p1w = separation.getPoint1();
        Vector2 p2w = separation.getPoint2();

        Vector2 r1 = p1w.cpy().sub(c1);
        Vector2 r2 = p2w.cpy().sub(c2);

        Vector2 n = separation.getNormal();
        float d = separation.getDistance();

        float C = MathUtils.clamp(d - MAX_LINEAR_TOLERANCE, -MAX_LINEAR_CORRECTION, 0.0f);

        float rn1 = VUtils.cross(r1, n);
        float rn2 = VUtils.cross(r2, n);

        float invMass1 = 0;
        float invMass2 = 0;
        float invI1 = 0;
        float invI2 = 0;

        float K = invMass1 + invMass2 + invI1 * rn1 * rn1 + invI2 * rn2 * rn2;

        float impulse = 0.0f;

        if(K > 0.0f) {
            impulse = -C / K;
        }

        Vector2 J = n.cpy().scl(impulse);

        body1.translate(J);
//        body1.rotate();
        //TODO FIX THIS.
    }
}