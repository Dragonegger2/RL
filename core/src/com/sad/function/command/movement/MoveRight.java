package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;

public class MoveRight implements GameCommand {

    private float acceleration;

    public MoveRight(float xVelocity) {
        this.acceleration = xVelocity;
    }

    @Override
    public void execute(World world, int entity) {
//        world.getMapper(PhysicsBody.class).create(entity).body.setLinearVelocity(acceleration, 0);
    }
}
