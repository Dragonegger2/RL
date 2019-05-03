package com.sad.function.command.movement;

import com.badlogic.ashley.core.Entity;
import com.sad.function.command.GameCommand;
import com.sad.function.components.Position;

public class MoveDown implements GameCommand {
    private float yVelocity;

    public MoveDown(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    @Override
    public void execute(Entity entity, float delta) {
        Velocity velocity = entity.getComponent(Velocity.class);

        if(velocity != null) {
           velocity.yVelocity += yVelocity;
        }
    }
}
