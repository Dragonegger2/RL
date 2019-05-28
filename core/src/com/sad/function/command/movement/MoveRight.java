package com.sad.function.command.movement;

import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.sad.function.command.GameCommand;
import com.sad.function.components.Velocity;

public class MoveRight implements GameCommand {

    private float acceleration;

    public MoveRight(float xVelocity) {
        this.acceleration = xVelocity;
    }

    @Override
    public void execute(World world, int entity) {
        Velocity v = world.getMapper(Velocity.class).create(entity);
        v.x = MathUtils.clamp(v.x += acceleration * world.delta, -acceleration, acceleration);    }
}
