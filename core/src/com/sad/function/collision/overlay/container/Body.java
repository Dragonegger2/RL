package com.sad.function.collision.overlay.container;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.Force;
import com.sad.function.collision.overlay.collision.broadphase.Collidable;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.shape.AbstractCollidable;
import com.sad.function.collision.overlay.shape.Convex;

import java.util.List;

public class Body extends AbstractCollidable<BodyFixture> implements Collidable<BodyFixture> {
    public static final float DEFAULT_LINEAR_DAMPING = 0.0f;
    public static final float DEFAULT_ANGULAR_DAMPING = 0.01f;

    private static final int AUTO_SLEEP = 1;
    private static final int ASLEEP = 2;
    private static final int ACTIVE = 4;
    private static final int ISLAND = 8;
    private static final int BULLET = 16;

    protected Vector2 velocity;
    protected float gravityScale;

    Transform transform0;
    Vector2 force;
    private int state;
    private List<Force> forces;
    private boolean asleep;

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
    public void setLinearVelocity(float x, float y) { this.velocity.set(x, y); }

    private void setAsleep(boolean b) {
        this.asleep = b;
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

    public boolean isActive() { return true; }

    public boolean isDynamic() { return true; }
}
