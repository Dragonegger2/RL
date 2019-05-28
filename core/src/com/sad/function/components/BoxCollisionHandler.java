package com.sad.function.components;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BoxCollisionHandler extends CollisionHandler {

    private static final Logger logger = LogManager.getLogger(BoxCollisionHandler.class);

    public BoxCollisionHandler(int id) {
        this.id = id;
    }

    @Override
    public void handleCollision(World world, int other, Vector2 penetrationVector) {
        switch (world.getMapper(Collidable.class).create(other).collisionCategory) {
            case PLAYER:
                world.getMapper(Velocity.class).create(id).x += penetrationVector.x;
                world.getMapper(Velocity.class).create(id).y += penetrationVector.y;
                break;

            case WALL:
                world.getMapper(Translation.class).create(id).x -= penetrationVector.x / 2;
                world.getMapper(Translation.class).create(id).y -= penetrationVector.y / 2;

                break;
            case BOX:
                world.getMapper(Translation.class).create(id).x -= penetrationVector.x / 4;
                world.getMapper(Translation.class).create(id).y -= penetrationVector.y / 4;

                world.getMapper(Translation.class).create(other).x += penetrationVector.x / 4;
                world.getMapper(Translation.class).create(other).y += penetrationVector.y / 4;
                break;

            case NULL:
            case NPC:
                break;
            default:
                logger.info("Unhandled case!");
        }
    }
}
