package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.sad.function.components.*;

public class TileFactory extends Factory{
    private World world;
    private Archetype tileArchetype;

    public TileFactory(World world) {
        this.world = world;

        tileArchetype = new ArchetypeBuilder()
                .add(Dimension.class)
                .add(TextureComponent.class)
                .build(world);
    }

    /**
     * Register the creation of a new wall with the world use to create this factory.
     * @param x position
     * @param y position
     * @return entity int.
     */
    public int create(float x, float y, String resourceName) {
        int tile = world.create(tileArchetype);

        world.getMapper(Translation.class).create(tile).setX(x).setY(y);
        world.getMapper(Layer.class).create(tile).layer = Layer.RENDERABLE_LAYER.GROUND;
        world.getMapper(TextureComponent.class).create(tile).resourceName = resourceName;

//        world.getMapper(PhysicsBody.class).create(tile).
        //TODO Should this be a box2d static body with sensors attached? https://stackoverflow.com/questions/2569374/friction-in-box2d
        return tile;
    }
}
