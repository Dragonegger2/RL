package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.broadphase.AbstractBroadphaseDetector;
import com.sad.function.collision.overlay.broadphase.BroadphasePair;
import com.sad.function.collision.overlay.broadphase.Sap;
import com.sad.function.collision.overlay.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.continuous.CA;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.filter.DetectBroadphaseFilter;
import com.sad.function.collision.overlay.narrowphase.CollisionManifold;
import com.sad.function.collision.overlay.narrowphase.SAT;
import com.sad.function.collision.overlay.shape.Convex;

import java.util.ArrayList;
import java.util.List;

public class World {
    private CA timeOfImpactSolver = new CA();
    private Vector2 gravity = new Vector2(0, -9.8f);
    public float step = 0;

    private final List<Body> bodies;
    private final List<Joint> joints;

    private NarrowPhaseDetector narrowphase = new SAT();
    private final AbstractBroadphaseDetector<Body, BodyFixture> broadphase = new Sap<>();



    protected BroadphaseFilter<Body, BodyFixture> detectBroadphaseFilter = new DetectBroadphaseFilter();
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
        for(int i = 0; i < bodies.size(); i++) {
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

            for(CollisionManifold manifold : manifolds) {
                Body b = manifold.body1.isStatic() ? body2 : body1;

                b.translate(manifold.normal.scl(manifold.distance));
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
}