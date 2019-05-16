package com.sad.function.World;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.sad.function.components.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class WorldGenerator {
    Logger logger = LogManager.getLogger(WorldGenerator.class);

    private Random random;

    private int WORLD_HEIGHT;
    private int WORLD_WIDTH;

    /**
     * Generator with no fixed seed.
     */
    public WorldGenerator(int width, int height) {
        random = new Random();
        WORLD_HEIGHT = height;
        WORLD_WIDTH = width;
    }

    /**
     * Create a world generator with a given seed.
     * @param seed value for random creation. Allows me to rebuild worlds.
     */
    public WorldGenerator(long seed, int width, int height) {
        random = new Random(seed);
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;
    }

    public void initializeWorld(Engine engine) {
        logger.info("Initializing world.");
        long startTime = System.nanoTime();
        //initialize world map.

        //Randomly fill it in with data.
        for(int x = 0; x < WORLD_WIDTH; x++ ) {
            for(int y = 0; y < WORLD_HEIGHT; y++ ) {
                engine.addEntity(new Entity()
//                        .add(new TextureComponent("tile-grass"))
                        .add(new Position().setX(x*32).setY(y*32).setZ(0))
//                        .add(new Dimension(32,32))
                );
            }
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        timeElapsed = timeElapsed / 1000000;
        logger.info("Finished world initialization in {} ms.", timeElapsed);
    }

    public int getWorldHeight() { return WORLD_HEIGHT; }
    public int getWorldWidth() { return WORLD_WIDTH; }
}
