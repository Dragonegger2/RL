package com.sad.function.components;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerCollisionHandler extends CollisionHandler {

    private static final Logger logger = LogManager.getLogger(PlayerCollisionHandler.class);

    public PlayerCollisionHandler(int id) {
        this.id = id;
    }

    @Override
    public void handleCollision(World world, int other, Vector2 penetrationVector) {
        switch (world.getMapper(Collidable.class).create(other).collisionCategory) {
            case BOX:
                logger.info("You touched a box!");
                world.getMapper(Position.class).create(id).x -= penetrationVector.x;
                world.getMapper(Position.class).create(id).y -= penetrationVector.y;

                break;
            case WALL:
                logger.info("You hit a wall!");
                world.getMapper(Position.class).create(id).x -= penetrationVector.x;
                world.getMapper(Position.class).create(id).y -= penetrationVector.y;

                break;
            default:
                logger.info("Unhandled case!");
        }
    }
}
