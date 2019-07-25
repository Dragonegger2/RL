package com.sad.function.command;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class MovementCommand implements GameCommand {
    public void clampSpeed(Body body, float MAX_SPEED) {
        float x = MathUtils.clamp(body.getLinearVelocity().x, -MAX_SPEED, MAX_SPEED);
        float y = MathUtils.clamp(body.getLinearVelocity().y, -MAX_SPEED, MAX_SPEED);

        body.setLinearVelocity(x, y);
    }
}
