package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;

public class Stop implements GameCommand {

    @Override
    public void execute(World world, int entity) {
//        world.getMapper(PhysicsBody.class).create(entity).body.setLinearVelocity(0,0);
    }
}
