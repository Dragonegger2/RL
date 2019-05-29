package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.*;
import com.sad.function.system.collision.shapes.Circle;
import com.sad.function.system.collision.shapes.Rectangle;

public class PlayerFactory extends Factory {
    private World world;

    private Archetype playerArchetype;

    public PlayerFactory(World world) {
        this.world = world;

        playerArchetype = new ArchetypeBuilder()
                .add(Animation.class)
                .add(Layer.class)
                .add(CameraComponent.class)
                .add(Input.class)
                .add(PhysicsBody.class)
                .build(world);
    }

    /**
     * Register the creation of a new wall with the world use to create this factory.
     *
     * @param x position
     * @param y position
     * @return entity int.
     */
    public int create(float x, float y) {
        int playerId = world.create(playerArchetype);

        float spriteSize = 32f / 32f;
        PhysicsBody pBody = world.getMapper(PhysicsBody.class).create(playerId);

        pBody.bodyShape = BodyShape.RECTANGLE; //We know what it's going to be, just store it here for convenience.

        pBody.setWidth(.5f);

        Translation translation = world.getMapper(Translation.class).create(playerId);
            translation.x = x;
            translation.y = y;

//        pBody.hitBox = new Circle(translation, .5f);
//        pBody.hitBox = new Rectangle(translation, new Vector2(0.5f, 0.5f));
        pBody.dynamic = true;

        pBody.hitbox = new org.dyn4j.geometry.Rectangle(0.5f, 0.5f);

        world.getEntity(playerId).getComponent(Layer.class).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        world.getMapper(Dimension.class).create(playerId)
                .setWidth(spriteSize)
                .setHeight(spriteSize);

        return playerId;
    }
}