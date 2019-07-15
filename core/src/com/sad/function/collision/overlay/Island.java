package com.sad.function.collision.overlay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.global.GameInfo;
import org.dyn4j.Epsilon;
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

        for(int i = 0; i <size;i++) {
            Body body = bodies.get(i);

            if(!body.isDynamic()) continue;

            body.accumulate(dt);

            invM = body.getMass().getInverseMass();
            invI = body.getMass().getInverseInertia();

            if(invM > Epsilon.E) {
                body.velocity.x += (body.force.x * invM + gravity.x * body.gravityScale) * dt;
                body.velocity.y += (body.force.y * invM + gravity.y * body.gravityScale) * dt;
            }

//            if (invI > Epsilon.E) {
//                // only perform this step if the body does not have
//                // a fixed angular velocity
//                body.angularVelocity += dt * invI * body.torque;
//            }

            // apply damping
            double linear = 1.0 - dt * body.linearDamping;
            double angular = 1.0 - dt * body.angularDamping;
            linear = MathUtils.clamp(linear, 0.0, 1.0);
            angular = MathUtils.clamp(angular, 0.0, 1.0);
            // inline body.velocity.multiply(linear);
            body.velocity.x *= linear;
            body.velocity.y *= linear;
            body.angularVelocity *= angular;
        }

        solver.initialize(contactConstraints);

        //initialize joint constraints
        //TODO
        //solve velocity constraints
        for(int i = 0; i < velocitySolverIterations; i++) {
            for(int j = 0; j < jSize; j++) {
                Joint joint = joints.get(j);

                joint.solveVelocityConstraints();
            }

            solver.solveVelocityConstraints(this.contactConstraints);
        }

        float maxTranslation = 10000f; //TODO Find a realistic value for this.
        float maxRotation = 0;
        float maxTranslationSqrd = maxTranslation * maxTranslation;

        //integrate the positions.
        for(int i = 0; i < size; i++) {
            Body body = bodies.get(i);

            if(body.isStatic()) continue;

            float translationX = body.velocity.x * dt;
            float translationY = body.velocity.y * dt;
            float translationMagnitudeSquared = translationX * translationX + translationY * translationY;

            if(translationMagnitudeSquared > maxTranslationSqrd) {
                float translationMagnitude = (float)Math.sqrt(translationMagnitudeSquared);
                float ratio = maxTranslation / translationMagnitude;
                
                body.velocity.scl(ratio);
                
                translationX *= ratio;
                translationY *= ratio;
            }
            
            float rotation = body.angularVelocity * dt;
            
            if(rotation > maxRotation) {
                float ratio = maxRotation / Math.abs(rotation);

                body.angularVelocity *= ratio;
                rotation *= ratio;
            }

            body.translate(translationX, translationY);
            body.rotateAboutCenter(rotation);
        }

        //solve position constraints.
        boolean positionConstraintsSolved = false;
        for(int i = 0; i < positionSolverIterations; i++) {
            boolean contactsSolved = solver.solvePositionConstraints(contactConstraints);
        }

    }

    public class ContactConstraintSolver {
        public void initialize(List<ContactConstraint> constraints) {

        }

        public void solveVelocityConstraints(List<ContactConstraint> contactConstraints) {

        }

        public boolean solvePositionConstraints(List<ContactConstraint> contactConstraints) {
            return true;
        }
    }
}
