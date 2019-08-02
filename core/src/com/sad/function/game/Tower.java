package com.sad.function.game;

import com.artemis.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sad.function.systems.EntitySpawnSystem;
import com.sad.function.collision.*;
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

//TODO: Add a PERSIST case to my ContactAdapter.
//TODO: Figure out a way to do one-way platforms.
//TODO: FIX SAP Broadphase Collision Detection. It shouldn't be using the Transform from the bodies anymore, it should continue passing along the component version.
//TODO: Write a rendering system.
//TODO: Handle player input via a system or something.


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

        //instantiate renderers.
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        //instantiate systems.
        physicsSystem = new PhysicsSystem();
        spawner = new EntitySpawnSystem();

        physicsSystem.addListener(new ContactAdapter() {
            @Override
            public void begin(Contact contact) {
                logger.info("NEW CONTACT {}:{} {} {}", contact.getId(), contact.hashCode(), contact.getFixture1().getTag(), contact.getFixture2().getTag());

                //Get fixture user data.
                Object fixture1UD = contact.getFixture1().getUserData();
                Object fixture2UD = contact.getFixture2().getUserData();

                //Skip if they're both null
                if(fixture1UD == null && fixture2UD == null) return;

                //If either is a foot sensor...
                if(fixture1UD == foot_sensor || fixture2UD == foot_sensor) {
                    contact.getFixture1().addContact(contact.getFixture2());
                    contact.getFixture2().addContact(contact.getFixture1());

                    footCount = fixture1UD == foot_sensor ? contact.getFixture1().contactCount() : contact.getFixture2().contactCount();
                }
            }

            @Override
            public void end(Contact contact) {
                Object fixture1UD = contact.getFixture1().getUserData();
                Object fixture2UD = contact.getFixture2().getUserData();

                if(fixture1UD == null && fixture2UD == null) return;

                if(fixture1UD == foot_sensor || fixture2UD == foot_sensor) {
                    contact.getFixture1().removeContact(contact.getFixture2());
                    contact.getFixture2().removeContact(contact.getFixture1());

                    footCount = fixture1UD == foot_sensor ? contact.getFixture1().contactCount() : contact.getFixture2().contactCount();
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

        int ground = spawner.assemblePlatform(0,0,10,0.0001f);
        mTransformComponent.create(ground).transform.translate(5f,0);

        int wall = spawner.assemblePlatform(0,0, 1, 100);
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
        //endregion
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

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && footCount > 0) {
            player.getVelocity().y = velocity;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            velocity += 5f;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            velocity -= 5f;
        }
        //endregion

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