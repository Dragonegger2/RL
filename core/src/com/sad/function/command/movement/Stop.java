package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;
import com.sad.function.components.VelocityComponent;

public class Stop implements GameCommand {

    private float acceleration;

    public Stop(float xVelocity) {
        this.acceleration = xVelocity;
    }

    @Override
    public void execute(World world, int entity, float delta) {
        world.getMapper(VelocityComponent.class).create(entity).x += acceleration * delta;
    }
}
