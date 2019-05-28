package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;
import com.sad.function.components.Velocity;

public class MoveHorizontally implements GameCommand {

    private float acceleration;

    public MoveHorizontally(float xVelocity) {
        this.acceleration = xVelocity;
    }

    @Override
    public void execute(World world, int entity) {
        world.getMapper(Velocity.class).create(entity).x += acceleration * world.delta;
    }
}
