package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sad.function.components.*;

@SuppressWarnings("Duplicates")
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
        //Add a physics body to the player ar
        PhysicsBody pBody = world.getMapper(PhysicsBody.class).create(playerId);
        pBody.body = createPBody(x, y, bodyRadius / 2);
        pBody.shape = PhysicsBody.BodyShape.CIRCLE; //Let's us use some logic elsewhere to calculate where we should be rendering.
        pBody.setWidth(bodyRadius);

        world.getEntity(playerId).getComponent(Layer.class).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        world.getMapper(Dimension.class).create(playerId)
                .setWidth(spriteSize)
                .setHeight(spriteSize)
                .setRenderOffset(new Vector2(0f,
                        offsetY));

        return playerId;
    }

    private Body createPBody(float x, float y, float radius) {
        Body pBody;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody; //Value that is willing and able to move!
        def.position.set(x, y);
        def.fixedRotation = true; //Don't rotate.

        pBody = pWorld.createBody(def);

        Shape shape = createCircle(radius);
        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }

    private Body createPBody(float x, float y, float width, float height) {
        Body pBody;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody; //Value that is willing and able to move!
        def.position.set(x, y);
        def.fixedRotation = true; //Don't rotate.

        pBody = pWorld.createBody(def);

        Shape shape = createBox(height, width);
        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }

    private PolygonShape createBox(float height, float width) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(height, width);
        return shape;
    }

    private CircleShape createCircle(float radius) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        return circleShape;
    }

}
