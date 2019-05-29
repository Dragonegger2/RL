package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.sad.function.components.*;

public class PlayerFactory extends Factory {
    private World world;
    private com.badlogic.gdx.physics.box2d.World pWorld;

    private Archetype playerArchetype;

    public PlayerFactory(World world, com.badlogic.gdx.physics.box2d.World pWorld) {
        this.world = world;
        this.pWorld = pWorld;

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

        world.getEntity(playerId).getComponent(Layer.class).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        world.getMapper(Dimension.class).create(playerId)
                .setWidth(spriteSize)
                .setHeight(spriteSize);

        pBody.body = new BodyCreator()
                .setBodyType(BodyDef.BodyType.DynamicBody)
                .setPosition(x, y)
                .hasFixedRotation(true)
                .buildBody(pWorld)
                .createBoxFixture(
                        spriteSize/2f,
                        spriteSize/2f,
                        1,
                        0.7f)
                .getBody();

        return playerId;
    }
}