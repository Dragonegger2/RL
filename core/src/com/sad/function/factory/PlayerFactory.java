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
                .add(Position.class)
                .add(Animation.class)
                .add(Dimension.class)
                .add(Layer.class)
                .add(CameraComponent.class)
                .add(Collidable.class)
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
    public int create(float x, float y, String resourceName) {
        int playerId = world.create(playerArchetype);

        //Don't need a position class.
        return playerId;
    }

    private Body createPBody(float x, float y, float width, float height) {
        Body pBody;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody; //Value that is willing and able to move!
        def.position.set(0, 0);
        def.fixedRotation = true; //Don't rotate.

        pBody = pWorld.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); //Box2d measures from the center.

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }
}
