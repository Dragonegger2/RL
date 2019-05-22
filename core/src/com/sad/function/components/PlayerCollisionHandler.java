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
                world.getMapper(Position.class).create(id).x -= penetrationVector.x / 2;
                world.getMapper(Position.class).create(id).y -= penetrationVector.y / 2;

                break;
            case WALL:
                world.getMapper(Position.class).create(id).x -= penetrationVector.x;
                world.getMapper(Position.class).create(id).y -= penetrationVector.y;

                break;
            default:
                logger.info("Unhandled case!");
        }
    }
}
