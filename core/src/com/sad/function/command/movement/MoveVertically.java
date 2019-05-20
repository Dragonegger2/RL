package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;
import com.sad.function.components.VelocityComponent;

public class MoveVertically implements GameCommand {
    private float acceleration;

    public MoveVertically(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void execute(World world, int entity) {
        world.getMapper(VelocityComponent.class).create(entity).y += acceleration * world.delta;
    }
}
