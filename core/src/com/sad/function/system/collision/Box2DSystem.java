package com.sad.function.system.collision;

import com.artemis.BaseSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.physics.box2d.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@All
public class Box2DSystem extends BaseSystem {
    private static final Logger logger = LogManager.getLogger(Box2DSystem.class);

    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private World pWorld;

    public Box2DSystem(World pWorld) {
        this.pWorld = pWorld;
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void processSystem() {
        pWorld.step(1/60f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }
}
