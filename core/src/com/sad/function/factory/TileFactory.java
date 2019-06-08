package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.sad.function.components.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.system.cd.EntityCategory.*;

public class TileFactory extends Factory{
    private static final Logger logger = LogManager.getLogger(TileFactory.class);
    private World world;
    private com.badlogic.gdx.physics.box2d.World pWorld;


    private Archetype tileArchetype;

    public TileFactory(World world, com.badlogic.gdx.physics.box2d.World pWorld) {
        this.world = world;
        this.pWorld = pWorld;

        tileArchetype = new ArchetypeBuilder()
                .add(Dimension.class)
                .add(TextureComponent.class)
                .build(world);
    }


    /**
     * Register the creation of a new wall with the world use to create this factory.
     * @param x origin
     * @param y origin
     * @return entity int.
     */
    public int create(float x, float y, String resourceName) {
        int tile = world.create(tileArchetype);

        world.getMapper(Translation.class).create(tile).setX(x).setY(y);
        world.getMapper(Layer.class).create(tile).layer = Layer.RENDERABLE_LAYER.GROUND;
        world.getMapper(TextureComponent.class).create(tile).resourceName = resourceName;

        world.getMapper(PhysicsBody.class).create(tile).body = new BodyCreator()
                .setPosition(x, y)
                .hasFixedRotation(true)
                .setBodyType(BodyDef.BodyType.StaticBody)
                .buildBody(pWorld)
                .createBoxFixture(1f, 1f, 0f, 0.75f)
                .getBody();

        //TODO Should this be a box2d static body with sensors attached? https://stackoverflow.com/questions/2569374/friction-in-box2d
        return tile;
    }

    private void createTiles(int width, int height) {
        double start = System.currentTimeMillis();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                create(x, y, "tile-grass");
            }
        }

        double end = System.currentTimeMillis();

        logger.info("Generated {} entities in {} ms.", width * height, end - start);
    }

}
