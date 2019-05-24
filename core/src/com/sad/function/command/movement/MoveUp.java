package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;
import com.sad.function.components.PhysicsBody;

public class MoveUp implements GameCommand {
    private float acceleration;

    public MoveUp(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void execute(World world, int entity) {
        world.getMapper(PhysicsBody.class).create(entity).body.setLinearVelocity(0, acceleration);

    }
}
