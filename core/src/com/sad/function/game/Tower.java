package com.sad.function.game;

import com.artemis.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sad.function.entities.ArchetypeDefinitions;
import com.sad.function.entities.EntitySpawnSystem;
import com.sad.function.entities.EntityType;
import com.sad.function.collision.*;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Rectangle;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;
import com.sad.function.global.GameInfo;
import com.sad.function.systems.CollisionBodyRenderingSystem;
import com.sad.function.systems.PhysicsSystem;
import com.sad.function.systems.SpriteRenderingSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.entities.EntityType.*;
import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class Tower extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(Tower.class);

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private ContactManager contactManager;

    private int footCount = 0;
    private int playerHealth = 100;

    private int jump = Input.Keys.SPACE;
    private int left = Input.Keys.LEFT;
    private int right = Input.Keys.RIGHT;

    private World gameWorld;

    private WorldConfiguration towerGameWorldConfig;

    protected ComponentMapper<TransformComponent> mTransformComponent;
    protected ComponentMapper<PhysicsBody> mPhysicsComponent;

    protected PhysicsSystem physicsSystem;
    protected EntitySpawnSystem spawner;

    private int playerID;
    private float velocity = 28f;

    //endregion

    @Override
    public void create() {
        camera = new OrthographicCamera();
        contactManager = new ContactManager();
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        physicsSystem = new PhysicsSystem();
        spawner = new EntitySpawnSystem();

        //foot contact counter.
        physicsSystem.addListener(new ContactAdapter() {
            @Override
            public void begin(Contact contact) {
                logger.info("NEW CONTACT {}:{} {} {}", contact.getId(), contact.hashCode(), contact.getFixture1().getTag(), contact.getFixture2().getTag());
                Object body1UD = contact.getBody1().getUserData();
                Object body2UD = contact.getBody2().getUserData();

                //handle player combo cases.
                if(body1UD == player || body2UD == player) {
                    if(body1UD == solid || body2UD == solid) {
                        //Have no way of linking the ids to this object.
                        logger.info("SOLID AND PLAYER COLLIDED");
                    }
                }

                Object fixture1UD = contact.getFixture1().getUserData();
                Object fixture2UD = contact.getFixture2().getUserData();

                if(fixture1UD == null && fixture2UD == null) return;

                if(fixture1UD == foot_sensor || fixture2UD == foot_sensor) {
                    footCount++;
                }
            }

            @Override
            public void end(Contact contact) {
                logger.info("CONTACT ENDED {}:{} {} {}", contact.getId(), contact.hashCode(), contact.getFixture1().getTag(), contact.getFixture2().getTag());

                Object fixture1UD = contact.getFixture1().getUserData();
                Object fixture2UD = contact.getFixture2().getUserData();

                if(fixture1UD == null && fixture2UD == null) return;

                if(fixture1UD == foot_sensor || fixture2UD == foot_sensor) {
                    footCount--;
                }
            }
        });

        towerGameWorldConfig = new WorldConfigurationBuilder()
                .with(
                        spawner,
                        new SpriteRenderingSystem(camera, spriteBatch),
                        new CollisionBodyRenderingSystem(camera, shapeRenderer),
                        physicsSystem
                )
                .build();


        gameWorld = new World(towerGameWorldConfig);

        //region Instantiate Component Mappers from Game World to facilitate entity creation.
        mTransformComponent = gameWorld.getMapper(TransformComponent.class);
        mPhysicsComponent = gameWorld.getMapper(PhysicsBody.class);

        //endregion

        //region Create game objects.
        playerID = spawner.player(2, 1.3112774f);
        logger.info("Created player: {}", playerID);


        int ground = spawner.assemblePlatform(0,0,10,1);
        mTransformComponent.create(ground).transform.translate(5f,0);
         logger.info("Created a solid {}", ground);

        int wall = spawner.assemblePlatform(0,0, 1, 100);
        logger.info("Created a solid {}", wall);

        int wall2 = spawner.assemblePlatform(10, 0, 1, 100);

        spawner.assembleSmallPlatform(10,0);
        spawner.assembleSmallPlatform(11,0);
        spawner.assembleSmallPlatform(12, 0);

        //TODO: Remove velocity component from the {@link Body}
        int s = spawner.assemblePlatform(3, 1, 1, 0.5f);
        mPhysicsComponent.create(s).body.getVelocity().set(0, 0.25f);
        mPhysicsComponent.create(s).body.setStatic(false);
        mPhysicsComponent.create(s).body.setUserData(platform);

        int bullet = spawner.assembleBullet(10, 1);
        logger.info("Created a bullet {}!", bullet);

        //endregion

        //TODO: Fix a bug where a moving platform automatically increments the footCount when it shouldn't be.
    }

    @Override
    public void render() {
        //Calculate minimum timestep.
        float delta = Math.min(Gdx.graphics.getDeltaTime(), GameInfo.DEFAULT_STEP_FREQUENCY);

        //region Input Handling
        Body player = mPhysicsComponent.create(playerID).body;
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getVelocity().x > -5f) {
            player.getVelocity().x -= GameInfo.MAX_HORIZONTAL_VELOCITY;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getVelocity().x < 5f) {
            player.getVelocity().x += GameInfo.MAX_HORIZONTAL_VELOCITY;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.getVelocity().x = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && footCount > 0) {
            player.getVelocity().y = velocity;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            velocity += 5f;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            velocity -= 5f;
        }
        //endregion

        //TODO: FIX SAP Broadphase Collision Detection. It shouldn't be using the Transform from the bodies anymore, it should continue passing along the component version.
        //TODO: Write a rendering system.
        //TODO: Handle player input via a system or something.


        gameWorld.setDelta(delta);
        gameWorld.process();

        Gdx.graphics.setTitle(String.format("|  FootCount: %s  |  FPS: %s |  Jump Speed: %s  |  Velocity: %s  |", footCount, Gdx.graphics.getFramesPerSecond(), velocity, mPhysicsComponent.create(playerID).body.getVelocity()));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}