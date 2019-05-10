package com.sad.function.command.movement;

import com.badlogic.ashley.core.Entity;
import com.sad.function.command.GameCommand;
import com.sad.function.components.VelocityComponent;

public class MoveVertically implements GameCommand {
    private float acceleration;

    public MoveVertically(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void execute(Entity entity, float delta) {
        VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);

        if (velocityComponent != null) {
            velocityComponent.y += acceleration * delta;
        }
    }
}
