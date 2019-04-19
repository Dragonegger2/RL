package com.sad.function.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sad.function.components.Velocity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DragSystem extends IteratingSystem {

    private static final Logger logger = LogManager.getLogger(DragSystem.class);

    public DragSystem() {
        super(Family.one(Velocity.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Velocity velocity = entity.getComponent(Velocity.class);
    }

}
