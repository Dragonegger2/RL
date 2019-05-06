package com.sad.function.command.movement;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.sad.function.command.GameCommand;
import com.sad.function.components.Velocity;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class MoveHorizontally implements GameCommand {
    private final ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);

    private float fixedVelocity;

    public MoveHorizontally(float xVelocity) {
        this.fixedVelocity = xVelocity;
    }

    @Override
    public void execute(Entity entity, float delta) {
        Velocity velocity = velocityMapper.get(entity);

        if(velocity != null) {
            velocity.x += fixedVelocity * delta;

            float signum = Math.signum(velocity.x);

            velocity.x = Math.abs(velocity.x);

            velocity.x = clamp(velocity.x,0,8);
            velocity.x *= signum;
        }
    }
}
