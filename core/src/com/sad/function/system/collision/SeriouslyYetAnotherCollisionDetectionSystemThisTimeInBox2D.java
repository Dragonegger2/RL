package com.sad.function.system.collision;

import com.artemis.BaseEntitySystem;
import com.badlogic.gdx.physics.box2d.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SeriouslyYetAnotherCollisionDetectionSystemThisTimeInBox2D extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(SeriouslyYetAnotherCollisionDetectionSystemThisTimeInBox2D.class);

    private static int VELOCITY_ITERATIONS = 6;
    private static int POSITION_ITERATIONS = 2;

    private World world;

    public SeriouslyYetAnotherCollisionDetectionSystemThisTimeInBox2D(World world) {
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(100,100);

        //DynamicBody
        //Kinematic
        //StaticBody -

        Body body = world.createBody(bodyDef);

        //Properties of a body:
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50,50);
        fixtureDef.shape = shape;
        Fixture fixture = body.createFixture(fixtureDef);

    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void processSystem() {
        logger.info("Updating physics engine.");
        world.step(1/60f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }
}
