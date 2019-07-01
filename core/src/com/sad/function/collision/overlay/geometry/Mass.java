package com.sad.function.collision.overlay.geometry;

import com.badlogic.gdx.math.Vector2;
import org.dyn4j.Epsilon;

public class Mass {
    private final float inertia;
    private final float invMass;      //1 / mass;
    private final float mass;
    private final float invInertia;
    private final Vector2 center;
    private MassType type;

    public Mass() {
        type = MassType.INFINITE;
        this.mass = 0.0f;
        this.inertia = 0.0f;
        this.invInertia = 0.0f;
        this.invMass = 0.0f;
        this.center = new Vector2();
    }

    public Mass(Vector2 center, float mass, float inertia) {
        if (center == null) throw new NullPointerException("CENTER OF MASS CANNOT BE NULL.");
        if (mass < 0.0f) throw new IllegalArgumentException("MASS CANNOT BE LESS THAN 0.0f");
        if (inertia < 0.0f) throw new IllegalArgumentException("INERTIA CANNOT BE LESS THAN 0.0f");

        //Create the mass;
        this.type = MassType.NORMAL;
        this.center = center.cpy();
        this.mass = mass;
        this.inertia = inertia;

        if (mass > Epsilon.E) {
            this.invMass = 1.0f / mass;
        } else {
            invMass = 0.0f;
            type = MassType.FIXED_LINEAR_VELOCITY;
        }

        if (inertia > Epsilon.E) {
            invInertia = 1.0f / inertia;
        } else {
            invInertia = 0.0f;
            type = MassType.FIXED_ANGULAR_VELOCITY;
        }

        if (mass <= Epsilon.E && inertia <= Epsilon.E) {
            type = MassType.INFINITE;
        }
    }

    public Vector2 getCenter() {
        return center;
    }

    public boolean isInfinite() {
        return type == MassType.INFINITE;
    }

    public MassType getType() {
        return this.type;
    }

    public void setType(MassType type) {
        this.type = type;
    }

    /**
     * Returns the mass or 0.0f if the mass is INFINITE or has a FIXED_LINEAR_VELOCITY.
     *
     * @return the mass or 0.0f if infinite or FIXED_LINEAR_VELOCITY
     */
    public float getMass() {
        if (type == MassType.INFINITE || type == MassType.FIXED_LINEAR_VELOCITY) {
            return 0.0f;
        }

        return mass;
    }

    public float getInverseMass() {
        if (type == MassType.INFINITE || type == MassType.FIXED_LINEAR_VELOCITY) {
            return 0.0f;
        }
        return invMass;
    }

    public float getInertia() {
        if (type == MassType.INFINITE || type == MassType.FIXED_ANGULAR_VELOCITY) {
            return 0.0f;
        }

        return inertia;
    }

    public float getInverseInertia() {
        if (type == MassType.INFINITE || type == MassType.FIXED_ANGULAR_VELOCITY) {
            return 0f;
        }
        return invInertia;
    }

    public enum MassType {
        NORMAL,
        INFINITE,
        FIXED_ANGULAR_VELOCITY,
        FIXED_LINEAR_VELOCITY
    }
}
