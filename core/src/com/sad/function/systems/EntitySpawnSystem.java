package com.sad.function.systems;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Rectangle;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.Lifetime;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;
import com.sad.function.entities.EntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.entities.EntityType.*;

/**
 * Helper system for adding entities to the game.
 */
public class EntitySpawnSystem extends BaseSystem {
    private static final Logger logger = LogManager.getLogger(EntitySpawnSystem.class);

    protected ComponentMapper<PhysicsBody> mPhysicsBody;
    protected ComponentMapper<TransformComponent> mTransformComponent;

    public EntitySpawnSystem() {
        aPlayer = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .add(GravityAffected .class)
                .build(world);

        aSolid = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .build(world);

        aBullet = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .build(world);

        aLimitedLifetimeSolid = new ArchetypeBuilder(aSolid)
                .add(Lifetime .class)
                .build(world);
    }
    @Override
    protected void processSystem() {}

    public int assembleBullet(float x, float y) {
        int e = world.create(aBullet);

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
        int solid = world.create(aSolid);
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

    /**
     * Instantiate all components related to a player.
     * @return the id for a new player.
     */
    public int player(float x, float y) {
        int e = world.create(aPlayer);

        //region Body Creation
        PhysicsBody cPhysics = mPhysicsBody.create(e);

        cPhysics.body = new Body();
        Body body = cPhysics.body;

        body.setStatic(false);
        body.setColor(Color.BLUE);
        body.setUserData(player);
        body.setUserData("PLAYER");
        Fixture footSensor = body.addFixture(new Rectangle(0.9f, 1f));
        footSensor.setSensor(true);
        footSensor.getShape().getCenter().set(0, -0.5f);
        footSensor.setUserData(foot_sensor);

        //Create the main collision body for the player
        body.addFixture(new Rectangle(1,1));
        //endregion

        TransformComponent cTransform = mTransformComponent.create(e);
        cTransform.transform = new Transform();
        cTransform.transform.translate(x, y);

        return e;
    }

    private Archetype aPlayer;

    private Archetype aSolid;

    private Archetype aBullet;

    private Archetype aLimitedLifetimeSolid;
}
