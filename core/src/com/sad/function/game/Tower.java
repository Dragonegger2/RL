package com.sad.function.game;

import com.artemis.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.ArchetypeDefinitions;
import com.sad.function.collision.*;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Rectangle;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;
import com.sad.function.global.GameInfo;
import com.sad.function.systems.CollisionBodyRenderingSystem;
import com.sad.function.systems.PhysicsSystem;
import com.sad.function.systems.SpriteRenderingSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private Object FOOT_SENSOR = new Object();
    private Object ARM_SENSOR = new Object();
    private Object PLAYER = new Object();
    private Object SOLID = new Object();
    private Object BULLET = new Object();

    private int jump = Input.Keys.SPACE;
    private int left = Input.Keys.LEFT;
    private int right = Input.Keys.RIGHT;

    private Archetype aPlayer;
    private Archetype aSolid;
    private Archetype aBullet;

    private World gameWorld;

    private WorldConfiguration towerGameWorldConfig;

    protected ComponentMapper<TransformComponent> mTransformComponent;
    protected ComponentMapper<PhysicsBody> mPhysicsComponent;

    protected PhysicsSystem physicsSystem;

    private int playerID;
    private float velocity = 28f;

    //region Entity Creation Methods
    /**
     * Instantiate all components related to a player.
     * @return the id for a new player.
     */
    private int createPlayer() {
        int e = gameWorld.create(aPlayer);

        //region Body Creation
        PhysicsBody cPhysics = mPhysicsComponent.create(e);
        cPhysics.body = new Body();
        Body body = cPhysics.body;

        body.setStatic(false);
        body.setColor(Color.BLUE);
        body.setUserData(PLAYER);
        body.setUserData("PLAYER");
        Fixture footSensor = body.addFixture(new Rectangle(0.9f, 1f));
        footSensor.setSensor(true);
        footSensor.getShape().getCenter().set(0, -0.5f);
        footSensor.setUserData(FOOT_SENSOR);

//
//        Fixture armSensor = body.addFixture(new Rectangle(2, 0.9f));
//        armSensor.setSensor(true);
//        armSensor.setUserData(ARM_SENSOR);
        //Create the main collision body for the player
        body.addFixture(new Rectangle(1,1));
        //endregion

        TransformComponent cTransform = mTransformComponent.create(e);
        cTransform.transform = new Transform();

        cTransform.transform.translate(2, 1.3112774f);

        return e;
    }

    private int createSolidRectangle(float width, float height, float x, float y, Color color) {
        int solid = gameWorld.create(aSolid);
        PhysicsBody cPhysics = mPhysicsComponent.create(solid);
        cPhysics.body = new Body();
        Body body = cPhysics.body;

        body.setStatic(true);
        body.addFixture(new Rectangle(width, height));
        body.setUserData(SOLID);
        body.setColor(color);
        body.setTag("SOLID");

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.transform = new Transform();
        transformComponent.transform.translate(x, y);

        return solid;
    }

    private int createBullet() {
        int e = gameWorld.create(aBullet);

        PhysicsBody cPhysics = mPhysicsComponent.create(e);
        cPhysics.body = new Body();
        Body b = cPhysics.body;

        b = new Body();
        b.setStatic(false);
        b.setGravityScale(0.0f);
        b.getVelocity().set(-1f, 0f);
        b.setUserData(BULLET);
        b.addFixture(new Rectangle(0.5f, 0.5f));
        b.setTag("BULLET");

        Transform t = mTransformComponent.create(e).transform;
        t = new Transform();

        t.translate(10f, 1f);

        return e;
    }

    //endregion

    @Override
    public void create() {
        camera = new OrthographicCamera();
        contactManager = new ContactManager();
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        physicsSystem = new PhysicsSystem();


        //foot contact counter.
        physicsSystem.addListener(new ContactAdapter() {
            @Override
            public void begin(Contact contact) {
                logger.info("NEW CONTACT {}:{} {} {}", contact.getId(), contact.hashCode(), contact.getFixture1().getTag(), contact.getFixture2().getTag());

                if(contact.getFixture1().getUserData() == FOOT_SENSOR || contact.getFixture2().getUserData() == FOOT_SENSOR) {
                    footCount++;
                }

//                if(contact.getFixture1().getUserData() == ARM_SENSOR || contact.getFixture2().getUserData() == ARM_SENSOR) {
////                    //TODO Add a check for a SOLID.
////                    //Figure out which one is the player.
////                    Body playerBody = contact.getFixture1().getUserData() == ARM_SENSOR ? contact.getBody1() : contact.getBody2();
////                    //Limit the verticle velocity.
////
////                    //Always negative.
////                    playerBody.setGravityScale(0.5f);
////                    //TODO: Set gravity scale back to normal.
////
////                }
            }

            @Override
            public void end(Contact contact) {
                logger.info("CONTACT ENDED {}:{} {} {}", contact.getId(), contact.hashCode(), contact.getFixture1().getTag(), contact.getFixture2().getTag());

                boolean footSensor = contact.getFixture1().getUserData() == FOOT_SENSOR || contact.getFixture2().getUserData() == FOOT_SENSOR;

                if(footSensor) {
                    footCount--;
                }
            }
        });

        towerGameWorldConfig = new WorldConfigurationBuilder()
                .with(
                        new SpriteRenderingSystem(camera, spriteBatch),
                        new CollisionBodyRenderingSystem(camera, shapeRenderer),
                        physicsSystem
                )
                .build();

        gameWorld = new World(towerGameWorldConfig);

        //region Archetypes Definitions.
        aPlayer = ArchetypeDefinitions.playerArchetype().build(gameWorld);

        aSolid = ArchetypeDefinitions.solidArchetype().build(gameWorld);

        aBullet = ArchetypeDefinitions.bulletARchetype().build(gameWorld);
        //endregion

        //region Instantiate Component Mappers from Game World to facilitate entity creation.
        mTransformComponent = gameWorld.getMapper(TransformComponent.class);
        mPhysicsComponent = gameWorld.getMapper(PhysicsBody.class);
        //endregion

        //region Create game objects.
        playerID = createPlayer();
        logger.info("Created player: {}", playerID);

        int ground = createSolidRectangle(10,1,0,0, Color.GREEN);
         logger.info("Created a solid {}", ground);

        int wall = createSolidRectangle(1, 100, 0,0, Color.GREEN);
        logger.info("Created a solid {}", wall);

        int bullet = createBullet();
        logger.info("Created a bullet {}!", bullet);
        //endregion

        //Handle contact with bullets.
//        physicsSystem.addListener(new ContactAdapter() {
//            @Override
//            public void begin(Contact contact) {
//                Object body1UserData = contact.getBody1().getUserData();
//                Object body2UserData = contact.getBody2().getUserData();
//
//                if((contact.getBody1().getUserData() == PLAYER || contact.getBody2().getUserData() == PLAYER) && (contact.getBody1().getUserData() == BULLET || contact.getBody2().getUserData() == BULLET)) {
//                    //Delete the bullet, and decrement player health.
//                    Body bullet = body1UserData == BULLET ? contact.getBody1() : contact.getBody2();
//
//                    //Need to add a way to get the ids of the things colliding.
//                    if(bodies.remove(bullet)) {
//                        playerHealth -= 10;
//                    } else {
//                        logger.error("Unable to remove the bullet from the bodies list.");
//                    }
//                }
//            }
//        });
    }

    @Override
    public void render() {
        Body player = mPhysicsComponent.create(playerID).body;
        Transform t = mTransformComponent.create(playerID).transform;

        //region Render Loop
        camera.position.x = t.getX();
        camera.position.y = t.getY();

        //endregion

        float delta = Gdx.graphics.getDeltaTime();

        //region Input Handling
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


        //TODO: FIX SAP Broadphase Collision Detection. It shouldn't be using the Transform from the bodies anymore,
        // it should continue passing along the component version.
        //TODO: Write a rendering system.
        //TODO: Handle player input via a system or something.

        gameWorld.setDelta(delta);
        gameWorld.process();

        Gdx.graphics.setTitle(String.format("|  FPS: %s |  Jump Speed: %s  |  Velocity: %s  |", Gdx.graphics.getFramesPerSecond(), velocity, gameWorld.getMapper(PhysicsBody.class).create(playerID).body.getVelocity()));
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