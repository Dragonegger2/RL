package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.broadphase.AbstractBroadphase;
import com.sad.function.collision.overlay.broadphase.BroadphasePair;
import com.sad.function.collision.overlay.broadphase.NSquared;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.continuous.CA;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.narrowphase.GJK;
import com.sad.function.collision.overlay.narrowphase.NarrowPhase;
import com.sad.function.collision.overlay.narrowphase.NarrowPhaseDetector;
import com.sad.function.collision.overlay.narrowphase.SAT;
import com.sad.function.collision.overlay.shape.Convex;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final AbstractBroadphase<Body, com.sad.function.collision.overlay.container.BodyFixture> broad;
    private final NarrowPhase narrow;
    private NarrowPhaseDetector narrowPhaseDetector = new SAT();
    private CA timeOfImpactSolver = new CA();
    private List<Body> bodies;
    private Vector2 gravity = new Vector2(0, -9.8f);

    public World() {
        bodies = new ArrayList<>(65);
        broad = new NSquared();
        narrow = new NarrowPhase();//TODO: Add ability to set detection type.
    }

    public World(AbstractBroadphase broad, NarrowPhase narrow) {
        this.broad = broad;
        this.narrow = narrow;

    }

    /**
     * Registers a body to be managed by the world.
     *
     * @param body to register.
     */
    public void addBody(Body body) {
        bodies.add(body);
        body.world = this;
        broad.add(body);
    }

    /**
     * @param body {@link Body}
     * @return if the bodies were removed.
     */
    public boolean removeBody(Body body) {
        broad.remove(body);
        return bodies.remove(body);
    }

    public void step(float delta) {
        updateBodies(delta);
    }

    public void detect(float delta) {
        int size = bodies.size();

        if(size > 0) {
            List<BroadphasePair<Body, BodyFixture>> pairs = broad.detect();

            int pSize = pairs.size();
            boolean allow = true;

            for(int i = 0; i < pSize; i++) {
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
                if(!fixture1.getFilter().isAllowed(fixture2.getFilter())) continue;

                if(this.narrowPhaseDetector.detect(convex1, transform1, convex2, transform2, penetration)) {
                    if(penetration.getDepth() == 0.0f) {
                        continue;
                    }

                    Body b = body1.isStatic() ? body2 : body1;
                    b.translate(penetration.normal.scl(penetration.distance));
                }
            }

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

    public List<Body> getBodies() {
        return bodies;
    }
}