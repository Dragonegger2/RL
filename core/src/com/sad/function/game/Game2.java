package com.sad.function.game;

import com.sad.function.components.CameraComponent;
import com.artemis.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sad.function.components.Dimension;
import com.sad.function.components.Layer;
import com.sad.function.components.Position;
import com.sad.function.components.TextureComponent;
import com.sad.function.manager.ResourceManager;
import com.sad.function.system.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game2 extends BaseGame {
    private static final Logger logger = LogManager.getLogger(Game2.class);

    private World world;
    private Camera camera;

    @Override
    public void create() {
        ResourceManager resourceManager = new ResourceManager();

        setupCamera();

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(
                        new InputSystem(),
                        new MovementSystem(),
                        new PhysicsSystem(),
                        new CameraSystem(camera),
                        new RenderingSystem(resourceManager, camera)
                )
                .build();

        world = new World(config);

        Archetype playerArchetype = new ArchetypeBuilder()
                .add(Position.class)
                .add(TextureComponent.class)
                .add(Dimension.class)
                .add(Layer.class)
                .add(CameraComponent.class)
                .build(world);

        ComponentMapper<Layer> layerMapper = world.getMapper(Layer.class);

        int player = world.create(playerArchetype);

        world.getEntity(player).getComponent(Position.class).x = 10;
        Dimension dim = world.getEntity(player).getComponent(Dimension.class);
        world.getEntity(player).getComponent(Layer.class).zIndex = 1;
        dim.width = 32;
        dim.height = 32;

        createTiles(world, 200, 100);
    }

    private void setupCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        float viewportWidth = 180;
        camera = new OrthographicCamera(viewportWidth,viewportWidth * (h/w));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight/ 2f, 0);
        camera.update();
    }

    private void createTiles(World world, int width, int height) {
        ComponentMapper<Position> mPosition = world.getMapper(Position.class);
        ComponentMapper<TextureComponent> mTexture = world.getMapper(TextureComponent.class);
        ComponentMapper<Layer> mLayer = world.getMapper(Layer.class);

        Archetype tileArchetype = new ArchetypeBuilder()
                //.add(Position.class)
                //.add(TextureComponent.class)
                //.add(Layer.class)
                .add(Dimension.class)
                .build(world);

        double start = System.currentTimeMillis();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int tile = world.create(tileArchetype);

                Position pos = mPosition.create(tile);
                pos.x = x * 32;
                pos.y = y * 32;

                mLayer.create(tile).zIndex = 0;

                mTexture.create(tile).resourceName = "tile-grass";
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

    }
}
