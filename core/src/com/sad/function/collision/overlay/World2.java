package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.broadphase.AbstractBroadphase;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.continuous.CA;
import com.sad.function.collision.overlay.data.TimeOfImpact;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.narrowphase.CollisionManifold;
import com.sad.function.collision.overlay.narrowphase.NarrowPhase;
import com.sad.function.collision.overlay.shape.Convex;

import java.util.ArrayList;
import java.util.List;

public class World2 {
    private final NarrowPhase narrow;
    private CA timeOfImpactSolver = new CA();
    private List<Body> bodies;
    private Vector2 gravity = new Vector2(0, -9.8f);

    public World2() {
        bodies = new ArrayList<>(65);
        narrow = new NarrowPhase();//TODO: Add ability to set detection type.
    }

    public World2(AbstractBroadphase broad, NarrowPhase narrow) {
        this.narrow = narrow;

    }

    /**
     * Registers a body to be managed by the world.
     *
     * @param body to register.
     */
    public void addBody(Body body) {
        bodies.add(body);
    }

    /**
     * @param body {@link Body}
     * @return if the bodies were removed.
     */
    public boolean removeBody(Body body) {
        return bodies.remove(body);
    }

    public void detect(float delta) {
        updateBodies(delta);

        List<CollisionManifold> manifolds;
        int size = bodies.size();
        for (int i = 0; i < size; i++) {
            Body body = bodies.get(i);
            if (body.isStatic()) continue;
            if (!body.isActive()) continue;

            detect(delta, bodies.get(i));
        }
    }

    public void detect(float delta, Body body1) {
        int size = bodies.size();
        for(int i = 0; i < size; i++) {
            Body body2 = bodies.get(i);

            if(body1 == body2) continue;


        }
    }


    private void updateBodies(float delta) {

        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            body.transform0.set(body.getTransform());
            accumulateBodyVelocity(body, delta);
        }

        for (int i = 0; i < bodies.size(); i++) {
            translateBodies(bodies.get(i), delta);
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

    private void translateBodies(Body body, float delta) {
        if (body.isStatic()) return;

        float translationX = body.velocity.x * delta;
        float translationY = body.velocity.y * delta;

        float rotation = body.angularVelocity * delta;

        body.translate(translationX, translationY);
        body.rotateAboutCenter(rotation);
    }

    private void solveTOI(float delta) {
        int size = bodies.size();

        for (int i = 0; i < size; i++) {
            Body body = bodies.get(i);

            if (!body.isBullet()) continue;

            solveToi(body, delta);
        }

    }

    private void solveToi(Body body1, float delta) {
        int size = bodies.size();

        AABB aabb1 = body1.createSweptAABB();
        boolean bullet = body1.isBullet();

        float t1 = 0.0f;
        float t2 = 1.0f;

        TimeOfImpact minToi = null;
        Body minBody = null;

        for (int i = 0; i < size; i++) {
            Body body2 = bodies.get(i);

            if (body1 == body2) continue;

            if (!body2.isActive()) continue;

            if (body2.isDynamic() && !bullet) continue;

            AABB aabb2 = body2.createSweptAABB();

            if (!aabb1.overlaps(aabb2)) continue;

            TimeOfImpact toi = new TimeOfImpact();
            int fc1 = body1.getFixtureCount();
            int fc2 = body2.getFixtureCount();

            Vector2 v1 = body1.getLinearVelocity().cpy().scl(delta);
            Vector2 v2 = body2.getLinearVelocity().cpy().scl(delta);
            float av1 = body1.getAngularVelocity() * delta;
            float av2 = body2.getAngularVelocity() * delta;

            Transform tx1 = body1.getInitialTransform();
            Transform tx2 = body2.getInitialTransform();

            for (int j = 0; j < fc1; j++) {
                BodyFixture f1 = body1.getFixture(j);

                if (f1.isSensor()) continue;

                for (int k = 0; k < fc2; k++) {
                    BodyFixture f2 = body2.getFixture(k);
                    if (f2.isSensor()) continue;

                    Filter filter1 = f1.getFilter();
                    Filter filter2 = f2.getFilter();

                    if (!filter1.isAllowed(filter2)) {
                        continue;
                    }

                    Convex c1 = f1.getShape();
                    Convex c2 = f2.getShape();

                    if (timeOfImpactSolver.solve(c1, tx1, v1, av1, c2, tx2, v2, av2, t1, t2, toi)) {
                        float t = toi.getTime();

                        if (t < t2) {
                            t2 = t;
                            minToi = toi;
                            minBody = body2;

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

                //TODO SOMETHING. https://github.com/dyn4j/dyn4j/blob/128bfd8b48c36b6e84446a0203b2dd55b67a1d94/src/main/java/org/dyn4j/dynamics/World.java#L1089
            }
        }
    }

    public List<Body> getBodies() {
        return bodies;
    }
}