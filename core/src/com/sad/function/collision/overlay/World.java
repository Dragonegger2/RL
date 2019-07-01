package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.broadphase.AbstractBroadphaseDetector;
import com.sad.function.collision.overlay.broadphase.BroadphasePair;
import com.sad.function.collision.overlay.broadphase.Sap;
import com.sad.function.collision.overlay.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.container.Fixture;
import com.sad.function.collision.overlay.continuous.ConservativeAdvancement;
import com.sad.function.collision.overlay.data.*;
import com.sad.function.collision.overlay.filter.DetectBroadphaseFilter;
import com.sad.function.collision.overlay.filter.Filter;
import com.sad.function.collision.overlay.geometry.Mass;
import com.sad.function.collision.overlay.narrowphase.CollisionManifold;
import com.sad.function.collision.overlay.narrowphase.GJK;
import com.sad.function.collision.overlay.shape.Convex;
import com.sad.function.global.GameInfo;
import org.dyn4j.Epsilon;

import java.util.ArrayList;
import java.util.List;

/**
 * Below has the logic for using restitution and friction.
 * https://github.com/dyn4j/dyn4j/blob/master/src/main/java/org/dyn4j/dynamics/contact/ContactConstraint.java
 */

public class World {
    private static final float MAX_LINEAR_CORRECTION = 0.2f;
    private static final float MAX_LINEAR_TOLERANCE = 0.005f;
    private static final float DEFAULT_MAX_TRANSLATION = 2.0f;
    private static final float DEFAUL_MAX_ROTATION = (float) (0.5f * Math.PI);

    public static boolean ContinuousCollisionDetection = true;
    public static boolean BulletsOnly = true;

    private final List<Body> bodies;
    private final List<Joint> joints;
    private final AbstractBroadphaseDetector<Body, BodyFixture> broadphase = new Sap<>();
    public float step = 0;
    protected BroadphaseFilter<Body, BodyFixture> detectBroadphaseFilter = new DetectBroadphaseFilter();
    private ConservativeAdvancement timeOfImpactSolver = new ConservativeAdvancement();
    private Vector2 gravity = new Vector2(0, -9.8f);
    private NarrowPhaseDetector narrowphase = new GJK();
    private float time;
    private final List<Listener> listeners;

    private boolean updateRequired = true;

    public World() {
        bodies = new ArrayList<>(65);
        joints = new ArrayList<>();

        listeners = new ArrayList<>();
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

    public void update(float elapsedTime) {
        this.update(elapsedTime, -1.0f, 1);
    }

    public boolean update(float elapsedTime, float stepElapsedTime, int maximumSteps) {
        if(elapsedTime < 0) elapsedTime = 0;

        step = elapsedTime;
        this.time += elapsedTime;

        //Inverse Frequency settings. This is for fixed step updates. Can add a method later to do variable time steps.
        float invHz = GameInfo.DEFAULT_STEP_FREQUENCY;
        int steps = 0;
        while(this.time >= invHz && steps < maximumSteps) {
            this.time = this.time - invHz;

            step(elapsedTime);//TODO Not sure this time makes sense. Hurk.
            steps++;
        }

        return steps > 0;
    }



    private void step(float delta) {
        List<StepListener> stepListeners = getListeners(StepListener.class);

        int sSize = stepListeners.size();
        for(int i = 0; i < sSize; i++) {
            stepListeners.get(i).begin(delta, this);
        }

        updateBodies();
        detect(delta);

//        solveTOI();
//        detect(delta);

        //Update the bodies by their new positions.
    }

    private void detect(float delta) {
        // if(ContinuousCollisionDetection) {
        //     solveTOI();
        // }

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

            //Handle all of my collisions.
            for (CollisionManifold manifold : manifolds) {
                Body b = manifold.body1.isStatic() ? body2 : body1;

                b.translate(manifold.normal.scl(manifold.distance));

                //Wait, I've already got the collision information...

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

    //Comes from the Island object in Dyn4j. That's how he chose to accumulate his forces.
    private void accumulateBodyVelocity(Body body, float delta) {
        if (!body.isDynamic()) return;

        body.accumulate(delta);

        float invM = body.getMass().getInverseMass();
//        float invI = body.getMass().getInverseInertia();

        //If the inverse mass is not infinite or fixed.
        if(invM > Epsilon.E) {
            body.velocity.x += (body.force.x * invM + gravity.x * body.gravityScale) * delta;
            body.velocity.y += (body.force.y * invM + gravity.y * body.gravityScale) * delta;

        }

        //TODO: Add torques if I want them.
//        if(invI > Epsilon.E) {
//            body.angularVelocity += delta * invI * body.torque;
//        }

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

    /**
     * Entry point for handling TOI's.
     */
    private void solveTOI() {
        //Listeners

        int size = bodies.size();
        for (int i = 0; i < size; i++) {
            Body body = bodies.get(i);

            if (BulletsOnly && !body.isBullet()) continue;//Skip if the body is a bullet.

            if (body.getMass().isInfinite()) continue;   //We're only handling non-static bodies.

            if(body.isAsleep()) continue;

            solveTOI(body);
        }
    }

    /**
     * Second stage of solving a TOI.
     * @param body1 a dynamic body that we are checking collisions with.
     */
    private void solveTOI(Body body1) {
        int size = bodies.size();

        AABB aabb1 = body1.createSweptAABB();
        boolean bullet = body1.isBullet();

        float t1 = 0.0f;
        float t2 = 1.0f;

        TimeOfImpact minToi = null;
        Body minBody = null;

        for (int i = 0; i < size; i++) {
            Body body2 = bodies.get(i);

            if (body1 == body2) continue; //Skip if they are the same body.
            if (body2.isDynamic() && !bullet)
                continue; //skip other dynamic to dynamic collisions. We only do that in the event of "bullets"

            AABB aabb2 = body2.createSweptAABB();

            if (!aabb1.overlaps(aabb2)) continue;//Don't test if they are overlapping.

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

            for (int j = 0; j < fc1; j++) {
                BodyFixture f1 = body2.getFixture(j);

                if (f1.isSensor()) continue; //we skip sensors.

                for (int k = 0; k < fc2; k++) {
                    BodyFixture f2 = body2.getFixture(k);

                    if (f2.isSensor()) continue;

                    Filter filter1 = f1.getFilter();
                    Filter filter2 = f2.getFilter();

                    //Ensure the fixtures are actually allowed to collide.
                    if (!filter1.isAllowed(filter2)) {
                        continue;
                    }

                    Convex c1 = f1.getShape();
                    Convex c2 = f2.getShape();

                    if (timeOfImpactSolver.solve(c1, tx1, v1, av1, c2, tx2, v2, av2, t1, t2, toi)) {
                        float t = toi.getTime();

                        if (t < t2) {
                            boolean allow = true;
                            //Alert all listeners if we want to.

                            if (allow) {
                                t2 = t;
                                minToi = toi;
                                minBody = body2;
                            }
                        }


                    }

                }
            }


        }

        if (minToi != null) {
            float t = minToi.getTime();

            body1.transform0.lerp(body1.getTransform(), t, body1.getTransform());

            if (minBody.isDynamic()) {
                minBody.transform0.lerp(minBody.getTransform(), t, minBody.getTransform());
            }

            timeOfImpactSolver(body1, minBody, minToi);
        }
    }

    /**
     * Solve TOI
     *
     * @param body1        in this impact
     * @param body2        in this impact
     * @param timeOfImpact collision information.
     */
    private void timeOfImpactSolver(Body body1, Body body2, TimeOfImpact timeOfImpact) {
        Vector2 c1 = body1.getWorldCenter();
        Vector2 c2 = body2.getWorldCenter();

        //Get Mass...
        Mass m1 = body1.getMass();
        Mass m2 = body2.getMass();

        float mass1 = m1.getMass();
        float mass2 = m2.getMass();

        float invMass1 = mass1 * m1.getInverseMass();
        float invI1 = mass1 * m1.getInverseInertia();
        float invMass2 = mass2 * m2.getInverseMass();
        float invI2 = mass2 * m2.getInverseInertia();

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

        float K = invMass1 + invMass2 + invI1 * rn1 * rn1 + invI2 * rn2 * rn2;

        float impulse = 0.0f;

        if (K > 0.0f) {
            impulse = -C / K;
        }

        Vector2 J = n.cpy().scl(impulse);

        body1.translate(J.scl(invMass1));
        body1.rotate(invI1 * r1.crs(J), c1.x, c1.y);

        body2.translate(J.scl(-invMass2));
        body2.rotate(-invI2 * r2.crs(J), c2.x, c2.y);
    }

    public AbstractBroadphaseDetector<Body, BodyFixture> getBroadphaseDetector() {
        return broadphase;
    }

    /**
     * Fetch all registered listeners to this world object that match the class type.
     * @param clazz listener type to fetch.
     * @param <T> Type parameter
     * @return list of listeners OR null if clazz is null.
     */
    public <T extends Listener> List<T> getListeners(Class<T> clazz) {
        if(clazz == null) return null;
        List<T> listeners = new ArrayList<T>();
        
        int lSize = this.listeners.size();
        for(int i = 0; i < lSize; i++) {
            Listener listener = this.listeners.get(i);
            if(clazz.isInstance(listener)) {
                listeners.add(clazz.cast(listener));
            }
        }

        return listeners;
    }


    public interface Listener {}
    public interface StepListener extends Listener {
        void begin(float delta, World world);
        void updatePerformed(float delta, World world);
        void postSolve(float delta, World world);
        void end(float delta, World world);
    }
    public interface ContactListener extends Listener {}
}