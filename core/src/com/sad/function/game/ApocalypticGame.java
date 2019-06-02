package com.sad.function.game;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sad.function.factory.PlayerFactory;
import com.sad.function.factory.WallEntityFactory;
import com.sad.function.global.GameInfo;
import com.sad.function.manager.ResourceManager;
import com.sad.function.system.*;
import com.sad.function.system.cd.MyContactListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

public class ApocalypticGame extends BaseGame {
    private static final Logger logger = LogManager.getLogger(ApocalypticGame.class);

    private World world;
    private com.badlogic.gdx.physics.box2d.World pWorld;
    private TiledMap tiledMap;
    private TiledMapRenderer myTiledMapRenderer;

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

        pWorld.setContactListener(new MyContactListener());

//        GameInfo.PLAYER = playerFactory.create(0.0f, 0.001f);
//        wallFactory.create(0f, 0, 10f, 1f);

        //Just a reminder that infinite tile maps are never supported. It would be best for me to create one in infinite mode and then scale it down.

        tiledMap = new TmxMapLoader().load("levels/level1.tmx");
         myTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/32f);

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

        Vector3 pos = camera.position;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.set(pos.x - 1f, pos.y, pos.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.set(pos.x + 1f, pos.y, pos.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.set(pos.x, pos.y + 1, pos.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.set(pos.x, pos.y - 1, pos.z);
        }
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myTiledMapRenderer.setView(camera);
        myTiledMapRenderer.render();
//        world.setDelta(Gdx.graphics.getDeltaTime());
//        world.process();

//        Gdx.graphics.setTitle(String.format("FPS: %s", Gdx.graphics.getFramesPerSecond()));
        Gdx.graphics.setTitle(String.format("Camera Position: (%s, %s)", camera.position.x, camera.position.y));
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
