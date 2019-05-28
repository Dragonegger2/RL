package com.sad.function.factory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.sad.function.components.*;

public class BoxFactory extends Factory {
    private World world;
    private com.badlogic.gdx.physics.box2d.World pWorld;

    private Archetype boxArchetype;

    public BoxFactory(World world, com.badlogic.gdx.physics.box2d.World pWorld) {
        this.world = world;
        this.pWorld = pWorld;

        boxArchetype = new ArchetypeBuilder()
                .add(Position.class)
                .add(TextureComponent.class)
                .add(Dimension.class)
                .add(PhysicsBody.class)
                .add(Layer.class)
                .build(world);
    }

    /**
     * Register the creation of a new wall with the world use to create this factory.
     *
     * @param x position
     * @param y position
     * @return entity int.
     */
    public int create(float x, float y, float width, float height) {
        int id = world.create(boxArchetype);

        world.getMapper(TextureComponent.class).create(id).resourceName = "box";
//        world.getMapper(PhysicsBody.class).create(id).body = new BodyCreator()
//                .hasFixedRotation(true)
//                .setPosition(x, y)
//                .setBodyType(BodyDef.BodyType.DynamicBody)
//                .buildBody(pWorld)
//                .createBoxFixture(width / 2f, height / 2f, 200.0f, 20f)
//                .getBody();

        world.getMapper(PhysicsBody.class).create(id).bodyShape = BodyShape.RECTANGLE;
        world.getMapper(PhysicsBody.class).create(id)
                .setWidth(width / 2)
                .setHeight(height / 2);


//        world.getMapper(PhysicsBody.class).create(id).body.setUserData(id);
        world.getMapper(PhysicsBody.class).create(id).density = 200.0f;
        world.getEntity(id).getComponent(Layer.class).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        world.getMapper(Dimension.class).create(id)
                .setWidth(1f)
                .setHeight(1f);


        return id;
    }

}
