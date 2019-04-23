package com.sad.function.command;

import com.badlogic.ashley.core.Entity;
import com.sad.function.components.Position;

public class MoveRight implements GameCommand {
    private float xVelocity;

    public MoveRight(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    @Override
    public void execute(Entity entity, float delta) {
        Position position = entity.getComponent(Position.class);

        if(position != null) {
            position.x += xVelocity * delta;
        }
    }
}
