package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.shape.AbstractCollidable;
import com.sad.function.collision.overlay.shape.Convex;
import org.dyn4j.geometry.Transform;

import java.awt.peer.ListPeer;
import java.util.ArrayList;
import java.util.List;

public class Body extends AbstractCollidable<BodyFixture> {
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

    private int state;

    Vector2 force;
    private List<Force> forces;
    private boolean asleep;

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
        if(fixture == null) throw new NullPointerException("BodyFixture can't be null!");
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


    private void setAsleep(boolean b) {
        this.asleep = b;
    }
}
