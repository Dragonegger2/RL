package com.sad.function.system;

import com.artemis.BaseSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.physics.box2d.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@All
public class PhysicsSystem extends BaseSystem {
    private static final Logger logger = LogManager.getLogger(PhysicsSystem.class);
    private World pWorld;

    public PhysicsSystem(World pWorld) {
        this.pWorld = pWorld;
    }

    @Override
    protected void processSystem() {
        //TODO Move those values to GameInfo soon.
        pWorld.step(world.delta, 6, 2);
    }
}
