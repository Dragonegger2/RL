package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;
import com.sad.function.components.VelocityComponent;

public class Stop implements GameCommand {

    @Override
    public void execute(World world, int entity, float delta) {
        world.getMapper(VelocityComponent.class).create(entity).x = 0;
        world.getMapper(VelocityComponent.class).create(entity).y = 0;
    }
}
