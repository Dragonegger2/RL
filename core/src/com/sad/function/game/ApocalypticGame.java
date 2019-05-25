package com.sad.function.game;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.factory.BoxFactory;
import com.sad.function.factory.PlayerFactory;
import com.sad.function.factory.TileFactory;
import com.sad.function.factory.WallEntityFactory;
import com.sad.function.manager.ResourceManager;
import com.sad.function.system.*;
import com.sad.function.system.collision.CDHeadbuttSystem;
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
    private BoxFactory boxFactory;

    private ResourceManager resourceManager;


    @Override
    public void create() {
        resourceManager = new ResourceManager();

        setupCamera();
        pWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(
                        new InputSystem(),
//                        new PhysicsSystem(),
//                        new Box2DSystem(pWorld),
                        new CDHeadbuttSystem(),
                        //Animation based systems
                        new CameraSystem(camera),
                        new AnimationSystem(),
                        new RenderingSystem(resourceManager, pWorld, camera)
                )
                .build();

        world = new World(config);

//        tileFactory = new TileFactory(world);
        wallFactory = new WallEntityFactory(world, pWorld);
        playerFactory = new PlayerFactory(world, pWorld);
//        boxFactory = new BoxFactory(world, pWorld);

        playerFactory.create(3, 3);
        wallFactory.createWall(100f, 100f, 2,2);
//        wallFactory.createWall(2, 2, 1, 1);
//        wallFactory.createWall(3, 3, 1, 1);
//
//        createTiles(100, 100);
//        boxFactory.create(0, 2, 1, 1);
//        boxFactory.create(0, 3, 1, 1);
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
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);


        //TODO Revisit the potato pixels.
        Matrix4 debugMatrix = new Matrix4(camera.combined);
        debugMatrix.translate((-Gdx.graphics.getWidth() / 2f), (-Gdx.graphics.getHeight() / 2f), 0);
        debugMatrix.scale(32f, 32f, 1f);
    }

    @Override
    public void dispose() {
        resourceManager.dispose();
    }
}