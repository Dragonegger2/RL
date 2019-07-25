package com.sad.function.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.badlogic.gdx.physics.box2d.World;
import com.sad.function.components.PhysicsBody;
import com.sad.function.global.GameInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@All
public class PhysicsSystem extends BaseSystem {
    private static final Logger logger = LogManager.getLogger(PhysicsSystem.class);
    private World pWorld;

    ComponentMapper<PhysicsBody> mPhysics;

    public PhysicsSystem(World pWorld) {
        this.pWorld = pWorld;
    }

    @Override
    protected void processSystem() {
        pWorld.step(world.delta, 6, 2);
    }
}
