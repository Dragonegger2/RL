package com.sad.function.collision.overlay.container;


import com.sad.function.collision.overlay.data.UserData;
import com.sad.function.collision.overlay.geometry.Mass;
import com.sad.function.collision.overlay.shape.Convex;

public class BodyFixture extends Fixture implements UserData {
    public static final float default_friction = 0.2f;
    public static final float default_restitution = 0.0f;
    public static final float default_density = 1.0f;
    public static Object userData;

    protected float density;
    protected float friction;
    protected float restitution;

    public BodyFixture(Convex shape) {
        super(shape);

        this.density = BodyFixture.default_density;
        this.friction = BodyFixture.default_friction;
        this.restitution = BodyFixture.default_restitution;
    }

    public float getDensity() {
        return this.density;
    }

    public void setDensity(float density) {
        if (density <= 0)
            throw new IllegalArgumentException("Invalid density.");
        this.density = density;
    }

    public float getFriction() {
        return this.friction;
    }

    public void setFriction(float friction) {
        if (friction < 0)
            throw new IllegalArgumentException("Invalid friction. Friction >= 0.");
        this.friction = friction;
    }

    public float getRestitution() {
        return this.restitution;
    }

    public void setRestitution(float restitution) {
        if (restitution < 0)
            throw new IllegalArgumentException("Illegal value for restitution.");
        this.restitution = restitution;
    }

    public boolean isSensor() {return sensor; }

    public void setSensor(boolean flag) { this.sensor = flag; }

    public Mass createMass() {
        return shape.createMass(density);
    }

    @Override
    public Object getUserData() {
        return userData;
    }

    @Override
    public void setUserData(Object userData) {
        this.userData = userData;
    }
}