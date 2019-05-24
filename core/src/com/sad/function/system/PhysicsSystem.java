package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.PhysicsBody;

public class PhysicsSystem extends IteratingSystem {

    private ComponentMapper<PhysicsBody> mPhysicsBody;

    public PhysicsSystem() {
        super(Aspect.all(PhysicsBody.class));
    }

    @Override
    protected void process(int entity) {
        PhysicsBody pBody = mPhysicsBody.create(entity);
        if(pBody.body.isAwake()) {
            applyAirFriction(pBody);
        }
    }

    private void applyAirFriction(PhysicsBody pBody) {
        float k = 1e-4f * pBody.density * pBody.getWidth() * pBody.getWidth();
        float velX = pBody.body.getLinearVelocity().x;
        float velY = pBody.body.getLinearVelocity().y;

        Vector2 dragForce = new Vector2(-k * pBody.body.getLinearVelocity().len() * velX, -k * pBody.body.getLinearVelocity().len() * velY);

        pBody.body.applyForce(dragForce, pBody.body.getWorldCenter(), false);


    }
}
