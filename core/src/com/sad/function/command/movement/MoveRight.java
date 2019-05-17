package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;
import com.sad.function.components.Animation;
import com.sad.function.components.VelocityComponent;

public class MoveRight implements GameCommand {

    private float acceleration;

    public MoveRight(float xVelocity) {
        this.acceleration = xVelocity;
    }

    @Override
    public void execute(World world, int entity, float delta) {
        world.getMapper(VelocityComponent.class).create(entity).x += acceleration * delta;

        if (world.getMapper(Animation.class).has(entity)) {
            world.getMapper(Animation.class).create(entity).direction = Animation.Direction.RIGHT;
        }
    }
}
