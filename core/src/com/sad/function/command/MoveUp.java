package com.sad.function.command;

import com.badlogic.ashley.core.Entity;
import com.sad.function.components.Position;

public class MoveUp implements GameCommand {
    private float yVelocity;

    public MoveUp(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    @Override
    public void execute(Entity entity, float delta) {
        Position position = entity.getComponent(Position.class);

        if(position != null) {
            position.y += yVelocity * delta;
        }
    }
}
