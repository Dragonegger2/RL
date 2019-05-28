package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.sad.function.components.Collidable;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Translation;
import com.sad.function.components.Velocity;
import com.sad.function.global.GameInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MovementSystem extends IteratingSystem {
    private static final Logger logger = LogManager.getLogger(MovementSystem.class);

    private ComponentMapper<Translation> mTranslation;
    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Collidable> mCollidable;
    private ComponentMapper<PhysicsBody> mPhysics;

    public MovementSystem() {
        super(Aspect.all(Translation.class, Velocity.class));
    }

    @Override
    protected void process(int entityId) {
        Translation translation = mTranslation.create(entityId);
        Velocity velocity = mVelocity.create(entityId);

        velocity.x = MathUtils.clamp(velocity.x, -GameInfo.MAX_MOVEMENT_SPEED, GameInfo.MAX_MOVEMENT_SPEED);
        velocity.y = MathUtils.clamp(velocity.y, -GameInfo.MAX_MOVEMENT_SPEED, GameInfo.MAX_MOVEMENT_SPEED);

        translation.x += velocity.x * world.delta;
        translation.y += velocity.y * world.delta;
    }
}
