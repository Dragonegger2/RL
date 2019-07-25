package com.sad.function.components;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NullHandler extends CollisionHandler{
    private static final Logger logger = LogManager.getLogger(NullHandler.class);

    @Override
    public void handleCollision(World world, int other, Vector2 penetrationVector) {
        logger.info("NO HANDLER REG'd.");
    }
}
