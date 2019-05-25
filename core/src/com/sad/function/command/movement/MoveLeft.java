package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;

public class MoveLeft implements GameCommand {

    private float MAX_SPEED;

    public MoveLeft(float xVelocity) {
        this.MAX_SPEED = xVelocity;
    }

    @Override
    public void execute(World world, int entity) {
//        world.getMapper(PhysicsBody.class).create(entity).body.setLinearVelocity(-MAX_SPEED, 0);
    }
}
