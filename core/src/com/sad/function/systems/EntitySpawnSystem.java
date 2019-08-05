package com.sad.function.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Rectangle;
import com.sad.function.components.*;
import com.sad.function.entities.EntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.entities.EntityType.*;

/**
 * Helper system for adding entities to the game.
 */
public class EntitySpawnSystem extends BaseSystem {
    private static final Logger logger = LogManager.getLogger(EntitySpawnSystem.class);

    private ComponentMapper<Bullet> mBullet;
    private ComponentMapper<Player> mPlayer;
    private ComponentMapper<PhysicsBody> mPhysicsBody;
    private ComponentMapper<GravityAffected> mGravityAffected;
    private ComponentMapper<TransformComponent> mTransformComponent;

    @Override
    protected void processSystem() {}

    public int assembleBullet(float x, float y, float vX, float vY) {
        int e = world.create();

        mBullet.create(e);
        mPhysicsBody.create(e).body.setStatic(false);
        mPhysicsBody.create(e).body.setGravityScale(0.0f);
        mPhysicsBody.create(e).body.getVelocity().set(vX, vY);

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
        int solid = world.create();
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
        int e = world.create();

        mGravityAffected.create(e);
        mPlayer.create(e);

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
}
