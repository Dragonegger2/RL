package com.sad.function.game;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.factory.PlayerFactory;
import com.sad.function.factory.WallEntityFactory;
import com.sad.function.global.GameInfo;
import com.sad.function.manager.ResourceManager;
import com.sad.function.system.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

public class ApocalypticGame extends BaseGame {
    private static final Logger logger = LogManager.getLogger(ApocalypticGame.class);

    private World world;
    private com.badlogic.gdx.physics.box2d.World pWorld;

    private OrthographicCamera camera;

    private ResourceManager resourceManager;

    @Override
    public void create() {
        resourceManager = new ResourceManager();

        camera = new OrthographicCamera();
        pWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.8f), true);

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(
                        new RenderingSystem(resourceManager, pWorld, camera),

                        new PlayerInputSystem(),
                        new PhysicsSystem(pWorld),
                        new CameraSystem(camera),
                        new AnimationSystem()
                )
                .build();

        world = new World(config);

        WallEntityFactory wallFactory = new WallEntityFactory(world, pWorld);
        PlayerFactory playerFactory = new PlayerFactory(world, pWorld);

        GameInfo.PLAYER = playerFactory.create(0.0f, 0.001f);
        wallFactory.create(0f, 0, 10f, 1f);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            GameInfo.RENDER_SPRITES = !GameInfo.RENDER_SPRITES;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            GameInfo.RENDER_SPRITE_OUTLINES = !GameInfo.RENDER_SPRITE_OUTLINES;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            GameInfo.RENDER_HITBOX_OUTLINES = !GameInfo.RENDER_HITBOX_OUTLINES;
        }

        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();

        Gdx.graphics.setTitle(String.format("FPS: %s", Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
    }

    @Override
    public void dispose() {
        resourceManager.dispose();
    }
}
