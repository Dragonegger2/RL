package com.sad.function.command.movement;

import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sad.function.command.GameCommand;
import com.sad.function.command.MovementCommand;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Velocity;

public class MoveRight extends MovementCommand {

    private float acceleration;

    public MoveRight(float xVelocity) {
        this.acceleration = xVelocity;
    }

    @Override
    public void execute(World world, int entity) {
        Body body = world.getMapper(PhysicsBody.class).create(entity).body;
        body.applyForce(new Vector2(1f, 0f), body.getWorldCenter(), true);

        clampSpeed(body, 10f);
    }
}
