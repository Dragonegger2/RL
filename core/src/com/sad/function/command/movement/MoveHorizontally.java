package com.sad.function.command.movement;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.sad.function.command.GameCommand;
import com.sad.function.components.VelocityComponent;

public class MoveHorizontally implements GameCommand {
    private final ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    private float acceleration;

    public MoveHorizontally(float xVelocity) {
        this.acceleration = xVelocity;
    }

    @Override
    public void execute(Entity entity, float delta) {
        VelocityComponent velocityComponent = velocityMapper.get(entity);

        if(velocityComponent != null) {
            velocityComponent.x += acceleration * delta;
        }
    }
}
