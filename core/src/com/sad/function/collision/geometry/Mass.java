package com.sad.function.collision.geometry;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.data.VUtils;
import org.dyn4j.Epsilon;

import java.util.List;

public class Mass {
    private MassType type;
    private final float inertia;
    private final float invMass;      //1 / mass;
    private final float mass;
    private final float invInertia;
    private final Vector2 center;

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

    public Mass(Mass mass) {
        if (mass == null) throw new NullPointerException("MASS CANNOT BE NULL IN COPY CONSTRUCTOR.");

        this.type = mass.type;
        this.center = mass.center.cpy();
        this.mass = mass.mass;
        this.inertia = mass.inertia;
        this.invMass = mass.invMass;
        this.invInertia = mass.invInertia;
    }

    public static Mass create(List<Mass> masses) {
        //TODO HANDLE The 0 and null cases.

        int size = masses.size();
        if (size == 1) {
            Mass m = masses.get(0);
            if (m != null) {
                return new Mass(masses.get(0));
            } else {
                throw new NullPointerException("INVALID MASS");
            }
        }

        Vector2 c = new Vector2();
        float m = 0.0f;
        float I = 0.0f;

        for (int i = 0; i < size; i++) {
            Mass mass = masses.get(i);
            if (mass == null) throw new NullPointerException("INVALID MASS");
            c.add(mass.center.cpy().scl(mass.mass));
            m += mass.mass;
        }

        if (m > 0.0f) {
            c.scl(1.0f / m);
        }

        for (int i = 0; i < size; i++) {
            Mass mass = masses.get(i);

            //Calcualte distance from new center to current mass's center.
            float d2 = VUtils.distanceSquared(mass.center, c);
            float Idis = mass.inertia + mass.mass * d2;

            I += Idis;

        }

        return new Mass(c, m, I);
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
