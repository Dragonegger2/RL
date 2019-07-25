package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.*;
import com.sad.function.collision.data.Penetration;
import com.sad.function.collision.detection.narrowphase.GJK;
import com.sad.function.collision.shape.Rectangle;
import com.sad.function.manager.LevelManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.sad.function.global.GameInfo.GRAVITY;
import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class Basic extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(Basic.class);
    private Body player;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private GJK gjk;
    private ContactManager contactManager;

    private Vector2 gravity = new Vector2(0, -9.8f);

    private List<Body> bodies = new ArrayList<>();
    private List<Listener> listeners = new ArrayList<>();

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

    LevelManager levelManager;

    @Override
    public void create() {
        gjk = new GJK();

        //TODO Rewrite the LevelManager so that it only accecpts an Artemis World object.
//        levelManager = new LevelManager();
        contactManager = new ContactManager();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        player = new Body();

        player.translate(2, 1.3112774f);
        player.setStatic(false);
        player.setColor(Color.BLUE);
        player.setUserData(PLAYER);
        Fixture footSensor = new Fixture(new Rectangle(.5f, 1f));
        footSensor.setSensor(true);
        footSensor.getShape().getCenter().set(0, -.5f); //Offset the fixture.
        footSensor.setUserData(FOOT_SENSOR);

        Fixture armSensor = new Fixture(new Rectangle(2, .9f));
        armSensor.setSensor(true);
        armSensor.setUserData(ARM_SENSOR);

        player.addFixture(armSensor);
        player.addFixture(footSensor);
        player.addFixture(new Rectangle(1f,1));

        Body ground = new Body();
        ground.setStatic(true);
        ground.setColor(Color.GREEN);
        ground.addFixture(new Rectangle(10, 1));
        ground.setUserData(SOLID);

        Body wall = new Body();
        wall.setStatic(true);
        wall.addFixture(new Rectangle(1, 100));
        wall.setUserData(SOLID);

        Gdx.graphics.setTitle("BASIC EXAMPLE");

        Body bulletExample = new Body();
        bulletExample.setStatic(false);
        bulletExample.addFixture(new Rectangle(0.5f, 0.5f));
        bulletExample.translate(10,1);
        bulletExample.setUserData(BULLET);
        bulletExample.getVelocity().set(-1f, 0);
        bulletExample.setGravityScale(0);

        bodies.add(bulletExample);
        bodies.add(ground);
        bodies.add(wall);
        bodies.add(player);

        //foot contact counter.
        listeners.add(new ContactAdapter() {
            @Override
            public void begin(Contact contact) {
                logger.info("NEW CONTACT {}:{} {} {}", contact.getId(), contact.hashCode(), contact.getFixture1().getTag(), contact.getFixture2().getTag());

                if(contact.getFixture1().getUserData() == FOOT_SENSOR || contact.getFixture2().getUserData() == FOOT_SENSOR) {
                    footCount++;
                }

                if(contact.getFixture1().getUserData() == ARM_SENSOR || contact.getFixture2().getUserData() == ARM_SENSOR) {
                    //TODO Add a check for a SOLID.
                    //Figure out which one is the player.
                    Body playerBody = contact.getFixture1().getUserData() == ARM_SENSOR ? contact.getBody1() : contact.getBody2();
                    //Limit the verticle velocity.

                    //Always negative.
                    playerBody.setGravityScale(0.5f);
                    //TODO: Set gravity scale back to normal.

                }
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

        //Handle contact with bullets.
        listeners.add(new ContactAdapter() {
            @Override
            public void begin(Contact contact) {
                Object body1UserData = contact.getBody1().getUserData();
                Object body2UserData = contact.getBody2().getUserData();

                if((contact.getBody1().getUserData() == PLAYER || contact.getBody2().getUserData() == PLAYER) && (contact.getBody1().getUserData() == BULLET || contact.getBody2().getUserData() == BULLET)) {
                    //Delete the bullet, and decrement player health.
                    Body bullet = body1UserData == BULLET ? contact.getBody1() : contact.getBody2();

                    if(bodies.remove(bullet)) {
                        playerHealth -= 10;
                    } else {
                        logger.error("Unable to remove the bullet from the bodies list.");
                    }
                }
            }
        });
    }

    @Override
    public void render() {
        //region Render Loop
        camera.position.x = player.getX();
        camera.position.y = player.getY();

        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            for(int j = 0; j < body.getFixtureCount(); j++) {
                Fixture f = body.getFixtures().get(j);

                Vector2 shapeCenter = f.getShape().getCenter();
                Vector2 position = body.getTransform().getTransformed(shapeCenter);

                if(f.getShape() instanceof Rectangle) {

                    Rectangle r = (Rectangle)f.getShape();


                    shapeRenderer.setColor(body.getColor());
                    if(f.isSensor()) shapeRenderer.setColor(Color.LIME);
                    shapeRenderer.rect(position.x - r.getWidth()/2, position.y - r.getHeight()/2, r.getWidth(), r.getHeight());
                } else {
                    logger.warn("No matching renderer for {}", f.getShape().getClass());
                }

                shapeRenderer.setColor(Color.FIREBRICK);
                shapeRenderer.circle(position.x, position.y, 0.125f, 15);

            }
        }
        shapeRenderer.end();

        float delta = 1f / 60f;   //TODO fix my timestep.
        //endregion

        //region Input Handling
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getVelocity().x > -5f) {
            player.getVelocity().x -= 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getVelocity().x < 5f) {
            player.getVelocity().x += 1f;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.getVelocity().x = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && footCount > 0) {
            player.getVelocity().y = 10f;
        }
        //endregion

        //region add gravity.
        int size = bodies.size();
        for (int i = 0; i < size; i++) {
            Body body = bodies.get(i);
            if (body.isStatic()) continue;
            float gravityScale = body.getGravityScale();

            body.getVelocity().add(gravity.cpy().scl(delta).scl(gravityScale));
        }

        //endregion

        int NUMBER_OF_STEPS = 1;
        float timestep = delta / NUMBER_OF_STEPS;
        for (int i = 0; i < NUMBER_OF_STEPS; i++) {
            //Move the bodies by their velocity/timestep
            for (int j = 0; j < size; j++) {
                Body body = bodies.get(j);
                if (body.isStatic()) continue;

                body.translate(body.getVelocity().cpy().scl(timestep));
            }

            handleBodies();
        }

        //Notify all listeners.
        contactManager.updateAndNotify(getListeners(ContactListener.class));

        Gdx.graphics.setTitle(String.format("FPS: %s  |  Velocity: %s  |  Position: %s  |  Foot Count: %s  |  Player Health: %s",
                Gdx.graphics.getFramesPerSecond(), player.getVelocity(), player.getPosition(), footCount, playerHealth));
    }

    private void handleBodies() {
        int size = bodies.size();
        for(int i = 0; i < bodies.size(); i++) {
            //skip non-dynamic bodies
            Body body = bodies.get(i);

            if(body.isStatic()) continue;

            checkFixtureCollisions(body);
        }
    }

    /**
     * Iterate through all fixtures contained within the param against all other bodies, pairwise. Would be simpler
     * if I used a broadphase collision handler first.
     *
     * @param body1 to check.
     */
    private void checkFixtureCollisions(Body body1) {
        int size = bodies.size();

        for(int i = 0; i < size; i++) {
            Body body2 = bodies.get(i);

            if(body1 == body2) continue;

            int fc1 = body1.getFixtureCount();
            int fc2 = body2.getFixtureCount();

            for(int j = 0; j < fc1; j++) {
                Fixture fixture1 = body1.getFixtures().get(j);
                for(int k = 0; k < fc2; k++) {
                    Fixture fixture2 = body2.getFixtures().get(k);

                    Penetration penetration = new Penetration();
                     if(gjk.detect(fixture1.getShape(), body1.getTransform(), fixture2.getShape(), body2.getTransform(), penetration)) {

                         //ToDO: Goign to need to modify this so that sensors on any body will work.
                        if(!fixture1.isSensor() && !fixture2.isSensor()) { //Neither are sensors...
                            //BODY1 is always the dynamic shape.
                            //All of this logic could be moved to a handler.
                            Vector2 translation = penetration.normal.cpy().scl(-1).scl(penetration.distance);
                            body1.translate(translation);

                            if(penetration.normal.x != 0) {
                                body1.getVelocity().x = 0;
                            }

                            if(penetration.normal.y != 0) {
                                body1.getVelocity().y = 0;
                            }
                        }

                        //Still need to notify if they are Sensors.
                        contactManager.addContact(new Contact(body1, fixture1, body2, fixture2, penetration));
                    }
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);

        //Try and set camera to point to the player. May cause jumping.
        if (player != null) {
            camera.position.x = (float) player.getX();
            camera.position.y = (float) player.getY();
        }
    }

    /**
     * Fetch all registered listeners to this world object that match the class type.
     * @param clazz listener type to fetch.
     * @param <T> Type parameter
     * @return list of listeners OR null if clazz is null.
     */
    public <T extends Listener> List<T> getListeners(Class<T> clazz) {
        if(clazz == null) return null;
        List<T> listeners = new ArrayList<T>();

        int lSize = this.listeners.size();
        for(int i = 0; i < lSize; i++) {
            Listener listener = this.listeners.get(i);
            if(clazz.isInstance(listener)) {
                listeners.add(clazz.cast(listener));
            }
        }

        return listeners;
    }
}