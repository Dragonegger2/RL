package com.sad.function.game;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sad.function.components.*;
import com.sad.function.factory.TileFactory;
import com.sad.function.factory.WallEntityFactory;
import com.sad.function.manager.ResourceManager;
import com.sad.function.system.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game2 extends BaseGame {
    private static final Logger logger = LogManager.getLogger(Game2.class);

    private World world;
    private Camera camera;

    private TileFactory tileFactory;
    private WallEntityFactory wallFactory;

    private ResourceManager resourceManager;
    @Override
    public void create() {
        resourceManager = new ResourceManager();

        setupCamera();

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(
                        new TagManager(),
                        new InputSystem(),
                        new MovementSystem(),
                        new PhysicsSystem(),
                        new CollisionDetectionSystem(),
                        //Animation based systems
                        new CameraSystem(camera),
                        new AnimationSystem(),
                        new RenderingSystem(resourceManager, camera)
                )
                .build();

        world = new World(config);

        tileFactory = new TileFactory(world);
        wallFactory = new WallEntityFactory(world);

        createPlayer();


        createTiles(100, 100);
        createBox(150,150);
        createBox(190,190);

    }

    private void createPlayer() {
        Archetype playerArchetype = new ArchetypeBuilder()
                .add(Position.class)
                .add(Animation.class)
                .add(Dimension.class)
                .add(Layer.class)
                .add(CameraComponent.class)
                .add(Collidable.class)
                .add(Input.class)
                .build(world);

        int player = world.create(playerArchetype);

        wallFactory.createWall(32, 32, 32,32);
        wallFactory.createWall(64, 32, 32, 32);

        world.getEntity(player).getComponent(Position.class).x = 10;
        Dimension dim = world.getEntity(player).getComponent(Dimension.class);
        world.getMapper(Collidable.class).create(player)
                .setIsState(false)
                .setXOffset(6f)
                .setWidth(20f)
                .setHandler(new PlayerCollisionHandler(player)).setCollisionCategory(CollisionCategory.PLAYER);
        world.getEntity(player).getComponent(Layer.class).layer = Layer.RENDERABLE_LAYER.DEFAULT;

        dim.width = 32;
        dim.height = 32;
        world.getSystem(TagManager.class).register("PLAYER", player);

    }
    private void createBox(float x, float y) {
        Archetype boxArchetype = new ArchetypeBuilder()
                .add(Position.class)
                .add(TextureComponent.class)
                .add(Dimension.class)
                .add(Layer.class)
                .add(Collidable.class)
                .build(world);

        int box = world.create(boxArchetype);

        world.getMapper(TextureComponent.class).create(box).resourceName = "box";
        world.getMapper(Position.class).create(box).setX(x).setY(y);
        world.getMapper(Layer.class).create(box).layer = Layer.RENDERABLE_LAYER.DEFAULT;
        world.getMapper(Collidable.class).create(box).setIsState(false).setHeight(32f).setWidth(32f).setCollisionCategory(CollisionCategory.BOX);
        world.getMapper(Dimension.class).create(box).setDimensions(32f,32f);
    }

    private void setupCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        float viewportWidth = 180;
        camera = new OrthographicCamera(viewportWidth,viewportWidth * (h/w));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight/ 2f, 0);
        camera.update();
    }

    private void createTiles(int width, int height) {
        double start = System.currentTimeMillis();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tileFactory.create(x * 32, y * 32, "tile-grass");
            }
        }

        double end = System.currentTimeMillis();

        logger.info("Generated {} entities in {} ms.", width * height, end - start);
    }

    @Override
    public void render() {
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();

        Gdx.graphics.setTitle(String.format("FPS: %s", Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void dispose() {
        resourceManager.dispose();
    }
}
