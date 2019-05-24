package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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

        //Todo: Remove Position
        //TODO: Remove collidable
        //The physics body is the one with all my data.
        world.getMapper(PhysicsBody.class).create(playerId).body = createPBody(x, y, 32f/32f / 2, 32f/32f / 2);

        world.getEntity(playerId).getComponent(Layer.class).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        world.getMapper(Dimension.class).create(playerId).setDimensions(32.0f/32f, 32.0f/32f);

        //TODO Move offset logic to some other object.
        return playerId;
    }

    private Body createPBody(float x, float y, float width, float height) {
        Body pBody;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody; //Value that is willing and able to move!
        def.position.set(x, y);
        def.fixedRotation = true; //Don't rotate.

        pBody = pWorld.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); //Box2d measures from the center.

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }
}
