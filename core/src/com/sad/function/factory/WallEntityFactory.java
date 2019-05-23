package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.sad.function.components.*;
import com.sad.function.system.CollisionCategory;

public class WallEntityFactory extends Factory {
    private World world;
    private Archetype wallArchetype;
    private static NullHandler wallHandler;

    public WallEntityFactory(World world) {
        this.world = world;

        wallHandler = new NullHandler();

        wallArchetype = new ArchetypeBuilder()
                .add(Position.class)
                .add(Layer.class)
                .add(Dimension.class)
                .add(TextureComponent.class)
                .add(Collidable.class)
                .build(world);
    }

    /**
     * Register the creation of a new wall with the world use to create this factory.
     * @param x position
     * @param y position
     * @return entity int.
     */
    public int createWall(float x, float y, float width, float height) {
        int wall = world.create(wallArchetype);

        world.getMapper(Position.class).create(wall).x = x;
        world.getMapper(Position.class).create(wall).y = y;

        world.getMapper(Layer.class).create(wall).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        world.getMapper(Dimension.class).create(wall).setDimensions(width, height);
        world.getMapper(Collidable.class).create(wall).setDimensions(width, height);
        world.getMapper(Collidable.class).create(wall).isStatic = true;
        world.getMapper(Collidable.class).create(wall)
                .setCollisionCategory(CollisionCategory.WALL)
                .setHandler(wallHandler);

        world.getMapper(TextureComponent.class).create(wall).resourceName = "beaten_brick_tiled";
        return wall;
    }
}
