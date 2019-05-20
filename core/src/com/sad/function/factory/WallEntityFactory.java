package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.sad.function.components.*;

public class WallEntityFactory {
    private World world;
    private Archetype wallArchetype;

    public WallEntityFactory(World world) {
        this.world = world;

        wallArchetype = new ArchetypeBuilder()
                .add(Position.class)
                .add(Layer.class)
                .add(Dimension.class)
                .add(TextureComponent.class)
                .add(Collidable.class)
                .build(world);
    }

    /**
     * Register the creation of a new wall with the world use to initialize this factory.
     * @param x position
     * @param y position
     * @return entity int.
     */
    public int createWall(float x, float y, float width, float height) {
        int wall = world.create(wallArchetype);

        world.getMapper(Position.class).create(wall).x = x;
        world.getMapper(Position.class).create(wall).y = y;

        world.getMapper(Layer.class).create(wall).zIndex = 1;

        world.getMapper(Dimension.class).create(wall).setDimensions(width, height);
        world.getMapper(Collidable.class).create(wall).setDimensions(width, height);
        return wall;
    }
}
