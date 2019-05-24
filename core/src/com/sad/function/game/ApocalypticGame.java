package com.sad.function.game;

import com.artemis.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.*;
import com.sad.function.factory.PlayerFactory;
import com.sad.function.factory.TileFactory;
import com.sad.function.factory.WallEntityFactory;
import com.sad.function.manager.ResourceManager;
import com.sad.function.system.*;
import com.sad.function.system.collision.Box2DSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

public class ApocalypticGame extends BaseGame {
    private static final Logger logger = LogManager.getLogger(ApocalypticGame.class);

    private World world;
    private com.badlogic.gdx.physics.box2d.World pWorld;

    private OrthographicCamera camera;

    private TileFactory tileFactory;
    private WallEntityFactory wallFactory;
    private PlayerFactory playerFactory;

    private ResourceManager resourceManager;


    @Override
    public void create() {
        resourceManager = new ResourceManager();

        setupCamera();
        pWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0,0), true);

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(
                        new InputSystem(),
                        new Box2DSystem(pWorld),

                        //Animation based systems
                        new CameraSystem(camera),
                        new AnimationSystem(),
                        new RenderingSystem(resourceManager, pWorld, camera)
                )
                .build();

        world = new World(config);

        tileFactory = new TileFactory(world);
        wallFactory = new WallEntityFactory(world, pWorld);
        playerFactory = new PlayerFactory(world, pWorld);

        playerFactory.create(0,0);
        wallFactory.createWall(2, 2, 1,1);
        wallFactory.createWall(3, 3, 1, 1);

        createTiles(100, 100);
        createBox(150,150);
        createBox(190,190);
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
        world.getMapper(Collidable.class).create(box)
                .setIsState(false)
                .setHeight(16f)
                .setWidth(32f)
                .setCollisionCategory(CollisionCategory.BOX)
                .setHandler(new BoxCollisionHandler(box));

        world.getMapper(Dimension.class).create(box).setDimensions(32f,32f);
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
    }

    private void createTiles(int width, int height) {
        double start = System.currentTimeMillis();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tileFactory.create(x, y, "tile-grass");
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

        //TODO: Update the render method to render based on position
        //TODO: Add values to the PhysicsBody for the body height and width.
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float)height, VIRTUAL_HEIGHT);


        Matrix4 debugMatrix = new Matrix4(camera.combined);
        debugMatrix.translate((-Gdx.graphics.getWidth() / 2f), (-Gdx.graphics.getHeight() / 2f), 0);
        debugMatrix.scale(32f, 32f, 1f);
    }

    @Override
    public void dispose() {
        resourceManager.dispose();
    }
}
