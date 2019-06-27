package com.sad.function.collision.overlay;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.data.UserData;

import java.util.UUID;

public abstract class Joint extends Constraint implements UserData {
    protected final UUID id = UUID.randomUUID();

    protected boolean collisionAllowed;

    private Object userData;

    public Joint(Body body1, Body body2) {
        this(body1, body2, false);
    }

    public Joint(Body body1, Body body2, boolean collisionAllowed) {
        super(body1, body2);
        this.collisionAllowed = collisionAllowed;
    }

    public abstract void initializeConstraints();//TODO does it need step?
    public abstract boolean solvePositionConstraints();
    public abstract Vector2 getAnchor1();
    public abstract Vector2 getAnchor2();
    public abstract Vector2 getReactionForce(float invdt);
    public abstract double getReactionTorque(float invdt);

    @Override
    public Object getUserData() {
        return userData;
    }

    @Override
    public void setUserData(Object data) {
        this.userData = data;
    }
}
