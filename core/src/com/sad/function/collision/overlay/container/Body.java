package com.sad.function.collision.overlay.container;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.AABB;
import com.sad.function.collision.overlay.Force;
import com.sad.function.collision.overlay.World;
import com.sad.function.collision.overlay.broadphase.Collidable;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.AbstractCollidable;
import com.sad.function.collision.overlay.shape.Convex;
import org.dyn4j.Epsilon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Body extends AbstractCollidable<BodyFixture> implements Collidable<BodyFixture> {
    public static final float DEFAULT_LINEAR_DAMPING = 0.0f;
    public static final float DEFAULT_ANGULAR_DAMPING = 0.01f;
    private static final int AUTO_SLEEP = 1;
    private static final int ASLEEP = 2;
    private static final int ACTIVE = 4;
    private static final int ISLAND = 8;
    private static final int BULLET = 16;
    public World world;
    public Vector2 velocity;
    public float gravityScale;
    public float mass = 1;
    public Transform transform0;
    public Vector2 force;
    public float linearDamping = 1;
    public float angularDamping = 1;
    public float angularVelocity = 0;
    private int state;
    private List<Force> forces;
    private boolean asleep;
    private boolean dynamic;
    private float sleepTime;

    public Body() {
        this(1);
    }

    public Body(int fixtureCount) {
        super(fixtureCount);

        this.radius = 0.0f;
        //mass
        this.transform0 = new Transform();
        this.velocity = new Vector2();
        this.gravityScale = 1.0f;
        this.force = new Vector2();
        this.forces = new ArrayList<>(1);
        this.state = 0;
        this.state |= Body.AUTO_SLEEP;
        this.state |= Body.ACTIVE;
        this.sleepTime = 0.0f;
    }

    public BodyFixture addFixture(Convex convex) {
        return this.addFixture(convex, BodyFixture.default_density, BodyFixture.default_friction, BodyFixture.default_restitution);
    }

    private BodyFixture addFixture(Convex convex, float density, float friction, float restitution) {
        BodyFixture fixture = new BodyFixture(convex);
        fixture.setDensity(density);
        fixture.setFriction(friction);
        fixture.setFriction(restitution);

        this.fixtures.add(fixture);
        /*
        if(this.world != null) {
            this.world.braodphaseDetector.add(this, fixture);
        }
         */

        return fixture;
    }

    public Body addFixture(BodyFixture fixture) {
        if (fixture == null) throw new NullPointerException("BodyFixture can't be null!");
        this.fixtures.add(fixture);
        /*
        if(this.world != null) {
            this.world.braodphaseDetector.add(this, fixture);
        }
         */
        return this;
    }

    @Override
    public boolean removeFixture(BodyFixture fixture) {
        /*
        if(this.world != null) {
            this.world.broadphaseDetector.remove(this, fixture);
        }
         */
        return super.removeFixture(fixture);
    }

    @Override
    public Vector2 getLocalCenter() {
        return null;
    }

    public BodyFixture removeBodyFixture(int index) {
        BodyFixture fixture = super.removeFixture(index);
        /*
        if(this.world != null) {
            this.world.broadphaseDetector.remove(this,fixture);
        }
         */
        return fixture;
    }

    //TODO Remove All Fixtures
    //Remove Fixtures by point?
    //Mass calculations.

    //Apply force.

    public Vector2 getLinearVelocity() {
        return this.velocity;
    }

    public void setLinearVelocity(Vector2 velocity) {
        setLinearVelocity(velocity.x, velocity.y);
    }

    public void setLinearVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    public Vector2 getWorldCenter() {
        return new Vector2(transform.x, transform.y);
    }

    public boolean isBullet() {
        return (this.state & Body.BULLET) == Body.BULLET;
    }

    public void setBullet(boolean flag) {
        if (flag) {
            this.state |= Body.BULLET;
        } else {
            this.state &= ~Body.BULLET;
        }
    }

    public void setAsleep(boolean flag) {
        if (flag) {
            this.state |= Body.ASLEEP;
            this.velocity.setZero();
            this.angularVelocity = 0.0f;
            this.forces.clear();
            //torques.clear()
        } else {
            if ((this.state & Body.ASLEEP) == Body.ASLEEP) {
                this.sleepTime = 0.0f;
                this.state &= ~Body.ASLEEP;
            }
        }
    }
    public boolean isStatic() {
        return Math.abs(velocity.x) <= Epsilon.E &&
                Math.abs(velocity.y) <= Epsilon.E &&
                Math.abs(angularVelocity) <= Epsilon.E;
    }
    public boolean isActive() {
        return true;
    }

    public void setActive(boolean flag) {
        if (flag) {
            this.state |= Body.ACTIVE;
        } else {
            this.state &= ~Body.ACTIVE;
        }

    }

    public boolean isDynamic() {
        return (this.state & Body.ACTIVE) == Body.ACTIVE;
    }

    public void setDynamic(boolean flag) {
        this.dynamic = flag;
    }

    /**
     * Accumulate all of the forces currently being applied to the body and removes them if they've exceeded their life.
     *
     * @param delta since last called.
     */
    public void accumulate(float delta) {
        //clear the current forces.
        this.force.setZero();

        if (forces.size() > 0) {
            //update them all.
            Iterator<Force> forceIterator = forces.iterator();

            while (forceIterator.hasNext()) {
                Force f = forceIterator.next();
                force.add(f.getForce());
                if (f.isComplete(delta)) {
                    forceIterator.remove();
                }
            }
        }
    }

    public AABB createSweptAABB() {
        return createSweptAABB(this.transform0, this.transform);
    }

    private AABB createSweptAABB(Transform initialTransform, Transform finalTransform) {
        Vector2 iCenter = initialTransform.getTransformed(getWorldCenter());
        Vector2 fCenter = finalTransform.getTransformed(getWorldCenter());

        AABB swept = AABB.createAABBFromPoints(iCenter, fCenter);
        swept.expand(this.radius * 2f);

        return swept;
    }

    public float getAngularVelocity() {
        return this.angularVelocity;
    }

    public Transform getInitialTransform() {
        return this.transform0;
    }
}
