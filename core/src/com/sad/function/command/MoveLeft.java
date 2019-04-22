package com.sad.function.command;

import com.badlogic.ashley.core.Entity;
import com.sad.function.components.Velocity;

public class MoveLeft implements GameCommand {

    @Override
    public void execute(Entity entity, float delta) {
        Velocity velocity = entity.getComponent(Velocity.class);

        if(velocity != null) {
            float velocityX = -0.5f;
            velocity.xVelocity -= velocityX * delta;

            //TODO: Add a clamp function.
            float MAX_VELOCITY = -10f;
            if(velocity.xVelocity < MAX_VELOCITY) {
                velocity.xVelocity = MAX_VELOCITY;
            }
        }
    }
}
