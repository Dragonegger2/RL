package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.sad.function.components.Position;
import com.sad.function.components.VelocityComponent;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class PhysicsSystem extends IteratingSystem {
    private final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);

    public PhysicsSystem() {
        super(Family.all(VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        VelocityComponent velocityComponent = this.velocity.get(entity);

        float drag = 5f;

        velocityComponent.x = velocityComponent.x * (1 - delta * drag);
        velocityComponent.y = velocityComponent.y * (1 - delta * drag);
    }
}
