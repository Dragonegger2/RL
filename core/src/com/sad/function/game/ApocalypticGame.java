package com.sad.function.game;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sad.function.factory.PlayerFactory;
import com.sad.function.factory.WallEntityFactory;
import com.sad.function.global.GameInfo;
import com.sad.function.manager.LevelManager;
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

    private OrthographicCamera camera;
    private PlayerFactory playerFactory;
    private ResourceManager resourceManager;

    private LevelManager levelManager;
    @Override
    public void create() {
        resourceManager = new ResourceManager();


        camera = new OrthographicCamera();
        pWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -20), true);

        levelManager = new LevelManager(world, pWorld);
        levelManager.loadLevel("levels/level1.tmx");

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(
                        new RenderingSystem(resourceManager, pWorld, levelManager, camera),

                        new PlayerInputSystem(),
                        new PhysicsSystem(pWorld),
                        new CameraSystem(camera),
                        new AnimationSystem()
                )
                .build();

        world = new World(config);
        //Inject the physics world.
        world.inject(pWorld);

        playerFactory = new PlayerFactory(world, pWorld);

        pWorld.setContactListener(new MyContactListener());

        //Just a reminder that infinite tile maps are never supported. It would be best for me to create one in infinite mode and then scale it down.
        //Also a reminder, all methods having to do with the map object currently return their y-values normalized for the new direction of the origin.
    }

    private boolean pointOnce = true;

    @Override
    public void render() {
        //Point the camera to the start origin at least once.
        if(pointOnce) {
            Vector2 startPosition = new Vector2();
            ((RectangleMapObject)levelManager.getMapObjects().get("STARTING_POINT")).getRectangle().getPosition(startPosition);
            startPosition.scl(1/16f);

            camera.translate(startPosition);
            GameInfo.PLAYER = playerFactory.create(startPosition.x, startPosition.y);
            pointOnce = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            GameInfo.RENDER_SPRITES = !GameInfo.RENDER_SPRITES;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            GameInfo.RENDER_SPRITE_OUTLINES = !GameInfo.RENDER_SPRITE_OUTLINES;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            GameInfo.RENDER_HITBOX_OUTLINES = !GameInfo.RENDER_HITBOX_OUTLINES;
        }

        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();

        Gdx.graphics.setTitle(String.format("FPS: %s | Cam: (%s, %s)", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y));
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
