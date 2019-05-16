package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.sad.function.components.Position;
import com.sad.function.components.VelocityComponent;
import com.sad.function.global.Global;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MovementSystem extends IteratingSystem {
    private static final Logger logger = LogManager.getLogger(MovementSystem.class);

    private ComponentMapper<Position> mPosition;
    private ComponentMapper<VelocityComponent> mVelocity;

    public MovementSystem() {
        super(Aspect.all(Position.class, VelocityComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Position position = mPosition.create(entityId);
        VelocityComponent velocityComponent = mVelocity.create(entityId);

        velocityComponent.x = MathUtils.clamp(velocityComponent.x, -Global.MAX_MOVEMENT_SPEED, Global.MAX_MOVEMENT_SPEED);
        velocityComponent.y = MathUtils.clamp(velocityComponent.y, -Global.MAX_MOVEMENT_SPEED, Global.MAX_MOVEMENT_SPEED);

        position.x += velocityComponent.x * Global.DELTA;
        position.y += velocityComponent.y * Global.DELTA;

        logger.debug("Entity Position: {},{}", position.x, position.y);
    }
}
