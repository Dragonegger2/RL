package com.sad.function.command.movement;

import com.badlogic.ashley.core.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sad.function.command.GameCommand;
import com.sad.function.components.Position;

public class MoveHorizontally implements GameCommand {
    private float xVelocity;

    public MoveHorizontally(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    @Override
    public void execute(Entity entity, float delta) {
        Velocity velocity = entity.getComponent(Velocity.class);

        if(velocity != null) {
            velocity.xVelocity += xVelocity * delta;
        }
    }
}
