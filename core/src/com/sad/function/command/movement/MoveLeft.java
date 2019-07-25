package com.sad.function.command.movement;

import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sad.function.command.GameCommand;
import com.sad.function.command.MovementCommand;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Velocity;
import com.sad.function.global.GameInfo;

public class MoveLeft extends MovementCommand {
    @Override
    public void execute(World world, int entity) {
        Body body = world.getMapper(PhysicsBody.class).create(entity).body;
        body.applyForce(-GameInfo.XVelocity, 0f, body.getWorldCenter().x, body.getWorldCenter().y, true);
    }
}
