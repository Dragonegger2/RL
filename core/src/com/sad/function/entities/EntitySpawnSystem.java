package com.sad.function.entities;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.sad.function.collision.Body;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Rectangle;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.entities.EntityType.bullet;

public class EntitySpawnSystem extends BaseSystem {
    private static final Logger logger = LogManager.getLogger(EntitySpawnSystem.class);

    protected ComponentMapper<PhysicsBody> mPhysicsBody;
    protected ComponentMapper<TransformComponent> mTransformComponent;

    @Override
    protected void processSystem() {}

    public int assembleBullet(float x, float y) {
        int e = world.create(ArchetypeDefinitions.aBullet.build(world));

        mPhysicsBody.create(e).body.setStatic(false);
        mPhysicsBody.create(e).body.setGravityScale(0.0f);
        mPhysicsBody.create(e).body.getVelocity().set(-1f, 0f);
        mPhysicsBody.create(e).body.setUserData(bullet);
        mPhysicsBody.create(e).body.addFixture(new Rectangle(0.5f, 0.5f));
        mPhysicsBody.create(e).body.setTag("BULLET");

        mTransformComponent.create(e).transform = new Transform();
        mTransformComponent.create(e).transform.translate(x, y);

        return e;
    }

    public int assembleSmallPlatform(float x, float y) {
        return assemblePlatform(x, y, 1, 0.5f);
    }

    public int assembleMediumPlatform(float x, float y) {
        return assemblePlatform(x, y, 2, 0.5f);
    }

    public int assemblePlatform(float x, float y, float width, float height) {
        int solid = world.create(ArchetypeDefinitions.aSolid.build(world));
        PhysicsBody cPhysicsBody = mPhysicsBody.create(solid);
        Body body = cPhysicsBody.body;

        body.setStatic(true);
        body.addFixture(new Rectangle(width, height));
        body.setUserData(EntityType.solid);
        body.setTag("SOLID");

        TransformComponent transformComponent = mTransformComponent.create(solid);
        transformComponent.transform = new Transform();
        transformComponent.transform.translate(x, y);

        logger.info("Created platform with world id {} and a unique body id of {}", solid, body.getId());
        return solid;
    }
}
