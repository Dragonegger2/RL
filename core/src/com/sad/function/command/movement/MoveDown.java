package com.sad.function.command.movement;

import com.artemis.World;
import com.sad.function.command.GameCommand;
import com.sad.function.components.Animation;
import com.sad.function.components.VelocityComponent;

public class MoveDown implements GameCommand {
    private float acceleration;

    public MoveDown(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void execute(World world, int entity, float delta) {
        world.getMapper(VelocityComponent.class).create(entity).y -= acceleration * delta;

        if (world.getMapper(Animation.class).has(entity)) {
            world.getMapper(Animation.class).create(entity).direction = Animation.Direction.DOWN;
        }
    }
}
