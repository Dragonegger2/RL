package com.sad.function.game;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sad.function.collision.Body;
import com.sad.function.collision.Contact;
import com.sad.function.collision.ContactAdapter;
import com.sad.function.collision.ContactManager;
import com.sad.function.components.Lifetime;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Player;
import com.sad.function.components.TransformComponent;
import com.sad.function.global.GameInfo;
import com.sad.function.systems.*;

import static com.sad.function.entities.EntityType.foot_sensor;
import static com.sad.function.entities.EntityType.platform;
import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

//TODO: Add a PERSIST case to my ContactAdapter.
//TODO: Fix issue with moving platforms.    https://gamedevelopment.tutsplus.com/tutorials/platformer-mechanics-moving-platforms--cms-29344
//TODO: Figure out a way to do one-way platforms.
//TODO: FIX SAP Broadphase Collision Detection. It shouldn't be using the Transform from the bodies anymore, it should continue passing along the component version.
//TODO: Write a rendering system.
//TODO: Handle player input via a system or something.

@SuppressWarnings("ALL")
public class Tower extends ApplicationAdapter {
    private ComponentMapper<TransformComponent> mTransformComponent;
    private ComponentMapper<PhysicsBody> mPhysicsComponent;
    private ComponentMapper<Player> mPlayer;

    protected PhysicsSystem physicsSystem;
    protected EntitySpawnSystem spawnerSystem;

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

    private int playerID;
    private float velocity = 28f;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        contactManager = new ContactManager();

        //instantiate renderers.
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        //instantiate systems.
        physicsSystem = new PhysicsSystem();

        //TODO: If I implement a begin I can use that to check for platforms.
        physicsSystem.addListener(new PlayerContactAdapter());

        towerGameWorldConfig = new WorldConfigurationBuilder()
                .with(
                        new EntitySpawnSystem(),
                        new LifetimeSystem(),
                        physicsSystem,
                        new SpriteRenderingSystem(camera, spriteBatch),
                        new CollisionBodyRenderingSystem(camera, shapeRenderer)
                )
                .build();


        gameWorld = new World(towerGameWorldConfig);

        spawnerSystem = gameWorld.getSystem(EntitySpawnSystem.class);

        //region Instantiate Component Mappers from Game World to facilitate entity creation.
        mTransformComponent = gameWorld.getMapper(TransformComponent.class);
        mPhysicsComponent = gameWorld.getMapper(PhysicsBody.class);
        mPlayer = gameWorld.getMapper(Player.class);

        //endregion

        //region Create game objects.
        playerID = spawnerSystem.player(2, 1.3112774f);

        int ground = spawnerSystem.assemblePlatform(0, 0, 10, 0.0001f);
        mTransformComponent.create(ground).transform.translate(5f, 0);

        int wall = spawnerSystem.assemblePlatform(0, 0, 1, 100);
        int wall2 = spawnerSystem.assemblePlatform(50, 0, 1, 100);

        spawnerSystem.assembleSmallPlatform(10, 0);
        spawnerSystem.assembleSmallPlatform(11, 0);
        spawnerSystem.assembleSmallPlatform(12, 0);

        //TODO: Remove velocity component from the {@link Body}
        int s = spawnerSystem.assemblePlatform(3, 1, 1, 0.5f);
        mPhysicsComponent.create(s).body.getVelocity().set(0, 0.25f);
        mPhysicsComponent.create(s).body.setStatic(false);
        mPhysicsComponent.create(s).body.setUserData(platform);
        gameWorld.getMapper(Lifetime.class).create(s).lifetime = 2000;

        int bullet = spawnerSystem.assembleBullet(10, 1, -1, 0);

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            velocity += 5f;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            velocity -= 5f;
        }
        //endregion

        gameWorld.setDelta(delta);
        gameWorld.process();

//        Gdx.graphics.setTitle(String.format("|  FootCount: %s  |  FPS: %s |  Jump Speed: %s  |  Velocity: %s  |", footCount, Gdx.graphics.getFramesPerSecond(), velocity, mPhysicsComponent.create(playerID).body.getVelocity()));
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

    private class PlayerContactAdapter extends ContactAdapter {
        @Override
        public void begin(Contact contact) {
            //Get fixture user data.
            Object fixture1UD = contact.getFixture1().getUserData();
            Object fixture2UD = contact.getFixture2().getUserData();

            //Skip if they're both null
            if (fixture1UD == null && fixture2UD == null) return;

            //If either is a foot sensor...
            if (fixture1UD == foot_sensor || fixture2UD == foot_sensor) {
                //Add them as contacts.
//                    contact.getFixture1().addContact(contact.getFixture2());
//                    contact.getFixture2().addContact(contact.getFixture1());

                footCount = fixture1UD == foot_sensor ? contact.getFixture1().contactCount() : contact.getFixture2().contactCount();
            }

//                Player player = mPlayer.has(contact.getEntity1ID()) ? mPlayer.create(contact.getEntity1ID()) : mPlayer.create(contact.getEntity2ID());
//                player.footCount

        }

        @Override
        public void persist(Contact contacts) {}

        @Override
        public void end(Contact contact) {
            Object fixture1UD = contact.getFixture1().getUserData();
            Object fixture2UD = contact.getFixture2().getUserData();

            if (fixture1UD == null && fixture2UD == null) return;

            if (fixture1UD == foot_sensor || fixture2UD == foot_sensor) {
//                    contact.getFixture1().removeContact(contact.getFixture2());
//                    contact.getFixture2().removeContact(contact.getFixture1());

                footCount = fixture1UD == foot_sensor ? contact.getFixture1().contactCount() : contact.getFixture2().contactCount();
            }
        }
    }
}