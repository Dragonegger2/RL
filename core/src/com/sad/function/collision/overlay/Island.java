package com.sad.function.collision.overlay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.global.GameInfo;
import org.dyn4j.collision.Collisions;
import org.dyn4j.dynamics.Capacity;
import org.dyn4j.dynamics.contact.ContactConstraint;

import java.util.ArrayList;
import java.util.List;

final class Island {
    final List<Body> bodies;
    final List<Joint> joints;
    final List<ContactConstraint> contactConstraints;
    public Island() {
        this(Capacity.DEFAULT_CAPACITY);
    }

    public Island(Capacity initialCapacity) {
        this.bodies = new ArrayList<>(initialCapacity.getBodyCount());
        this.joints = new ArrayList<>(initialCapacity.getJointCount());
        int eSize = Collisions.getEstimatedCollisionPairs(initialCapacity.getBodyCount());
        this.contactConstraints = new ArrayList<>(eSize);
    }

    public void clear() {
        this.bodies.clear();
        this.contactConstraints.clear();
    }

    public void add(Body body) {
        this.bodies.add(body);
    }

    public void add(ContactConstraint contactConstraint) {
        this.contactConstraints.add(contactConstraint);
    }

    public void add(Joint joint) { this.joints.add(joint); }

    public void solve(ContactConstraintSolver solver, Vector2 gravity, float dt) {
        int velocitySolverIterations = GameInfo.VELOCITY_CONSTRAINT_SOLVER_TERATIONS;
        int positionSolverIterations = GameInfo.POSITION_CONSTRAINT_SOLVER_TERATIONS;

        float sleepAngularVelocity = 0.2f;
        float sleepLinearVelocitySquared = 0.25f;
        float sleepTime = 1000;

        int size = bodies.size();
        int jSize = joints.size();

        float invM, invI;
    }

    public class ContactConstraintSolver {}
}
