package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.sad.function.components.VelocityComponent;
import com.sad.function.global.Global;

public class PhysicsSystem extends IteratingSystem {
    private ComponentMapper<VelocityComponent> velocity;

    public PhysicsSystem() {
        super(Aspect.all(VelocityComponent.class));
    }

    @Override
    protected void process(int entity) {
        VelocityComponent velocityComponent = this.velocity.create(entity);

        float drag = 5f;

        velocityComponent.x = velocityComponent.x * (1 - Global.DELTA * drag);
        velocityComponent.y = velocityComponent.y * (1 - Global.DELTA * drag);
    }
}
