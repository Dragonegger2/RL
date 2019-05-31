package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sad.function.components.*;
import com.sad.function.global.GameInfo;
import com.sad.function.system.cd.EntityCategory;

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
                .add(PlayerComponent.class)
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
                        spriteSize / 2f,
                        spriteSize / 2f,
                        1,
                        0.1f)
                .getBody();

//        pBody.body.getFixtureList().first().setUserData("PLAYER_BODY");

        PolygonShape feet = new PolygonShape();
        feet.setAsBox(.25f, .25f, new Vector2(0, -.5f), 0); //TODO the Y in the vector needs to be offset by the height.

        FixtureDef myFixtureDef = new FixtureDef();
        myFixtureDef.shape = feet;
        myFixtureDef.isSensor = true;

        Fixture f = pBody.body.createFixture(myFixtureDef);
        f.setUserData("FOOT");

        feet.dispose();

        return playerId;
    }
}