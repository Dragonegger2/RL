package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.*;
import com.sad.function.system.collision.shapes.Circle;

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

        pBody.bodyShape = BodyShape.CIRCLE; //Let's us use some logic elsewhere to calculate where we should be rendering.

        pBody.setWidth(.5f);
        pBody.setHeight(.5f);

        pBody.hitBox = new Circle(new Vector2(x, y), .5f);//new Rectangle(new Vector2(x,y), new Vector2(.5f, .5f));//new Circle(new Vector2(x,y), bodyRadius/2);
        pBody.dynamic = true;

        world.getEntity(playerId).getComponent(Layer.class).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        world.getMapper(Dimension.class).create(playerId)
                .setWidth(spriteSize)
                .setHeight(spriteSize);

        return playerId;
    }

}
