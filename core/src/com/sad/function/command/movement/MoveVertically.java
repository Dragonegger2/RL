package com.sad.function.command.movement;

import com.badlogic.ashley.core.Entity;
import com.sad.function.command.GameCommand;
import com.sad.function.components.Velocity;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class MoveVertically implements GameCommand {
    private float yVelocity;

    public MoveVertically(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    @Override
    public void execute(Entity entity, float delta) {
        Velocity velocity = entity.getComponent(Velocity.class);

        if (velocity != null) {
            velocity.y += yVelocity * delta;

            float signum = Math.signum(velocity.y);

            velocity.y = Math.abs(velocity.y);

            velocity.y = clamp(velocity.y, 0, 8);
            velocity.y *= signum;
        }
    }
}
