package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
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
        float bodyRadius = 20f / 32f;
        float offsetY = -(spriteSize - bodyRadius)/2 ;

        PhysicsBody pBody = world.getMapper(PhysicsBody.class).create(playerId);
//        pBody.body = new BodyCreator().hasFixedRotation(true)
//                .setBodyType(BodyDef.BodyType.DynamicBody)
//                .setPosition(x, y)
//                .buildBody(pWorld)
//                .createCircleFixture(bodyRadius/2, 1.0f)
//                .getBody();

        pBody.shape = PhysicsBody.BodyShape.CIRCLE; //Let's us use some logic elsewhere to calculate where we should be rendering.
        pBody.setWidth(bodyRadius);
        pBody.position.set(x, y);


        //Currently only storing the id's of the objects in the user data section.
//        pBody.body.setUserData(playerId);

        world.getEntity(playerId).getComponent(Layer.class).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        world.getMapper(Dimension.class).create(playerId)
                .setWidth(spriteSize)
                .setHeight(spriteSize)
                .setRenderOffset(new Vector2(0f,
                        offsetY));

        return playerId;
    }

}
