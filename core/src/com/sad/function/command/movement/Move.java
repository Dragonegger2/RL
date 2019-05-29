package com.sad.function.command.movement;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sad.function.command.MovementCommand;
import com.sad.function.components.PhysicsBody;

public class Move extends MovementCommand {
    private float acceleration;

    public Move(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void execute(World world, int entity) {
        Body body = world.getMapper(PhysicsBody.class).create(entity).body;
        body.applyForce(new Vector2(0f, -1f), body.getWorldCenter(), true);

        clampSpeed(body, 10f);
    }
}
