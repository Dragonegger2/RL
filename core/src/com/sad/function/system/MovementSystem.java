package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.sad.function.components.Position;
import com.sad.function.components.VelocityComponent;
import com.sad.function.global.Global;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MovementSystem extends IteratingSystem {
    private static final Logger logger = LogManager.getLogger(MovementSystem.class);

    private final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    private final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);

    public MovementSystem() {
        super(Family.all(Position.class, VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        Position position = this.position.get(entity);
        VelocityComponent velocityComponent = this.velocity.get(entity);

        velocityComponent.x = MathUtils.clamp(velocityComponent.x, -Global.MAX_MOVEMENT_SPEED, Global.MAX_MOVEMENT_SPEED);
        velocityComponent.y = MathUtils.clamp(velocityComponent.y, -Global.MAX_MOVEMENT_SPEED, Global.MAX_MOVEMENT_SPEED);

        position.x += velocityComponent.x * delta;
        position.y += velocityComponent.y * delta;

        logger.debug("Entity Position: {},{}", position.x, position.y);
    }
}