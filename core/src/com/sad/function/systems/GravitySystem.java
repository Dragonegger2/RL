package com.sad.function.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.Body;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.PhysicsBody;


/**
 * Add gravity to ever {@link PhysicsBody} if they are {@link GravityAffected}.
 */
@All({PhysicsBody.class, GravityAffected.class})
public class GravitySystem extends IteratingSystem {
    private final Vector2 gravity = new Vector2(0, -9.8f);

    public GravitySystem() {}
    public GravitySystem(Vector2 gravity) { this.gravity.set(gravity); }

    protected ComponentMapper<PhysicsBody> mPhysicsBody;
    protected ComponentMapper<GravityAffected> mGravityAffected;

    @Override
    protected void process(int entityId) {
        PhysicsBody physicsBody = mPhysicsBody.create(entityId);
        Body body = physicsBody.body;

        GravityAffected gravityAffected = mGravityAffected.create(entityId);

        body.getVelocity().add(gravity.cpy().scl(gravityAffected.gravityScale));
    }
}
