package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.*;
import com.sad.function.collision.data.Penetration;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.detection.narrowphase.GJK;
import com.sad.function.collision.shape.Convex;
import com.sad.function.collision.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

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
    private Object PLAYER = new Object();
    private Object SOLID = new Object();
    private Object BULLET = new Object();

    private int jump = Input.Keys.SPACE;
    private int left = Input.Keys.LEFT;
    private int right = Input.Keys.RIGHT;

    @Override
    public void create() {
        gjk = new GJK();

        contactManager = new ContactManager();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        player = new Body();

        player.translate(2, 1.3112774f);
        player.setStatic(false);
        player.setColor(Color.BLUE);
        player.setUserData(PLAYER);
        Fixture footSensor = new Fixture(new Rectangle(.5f, 1f), "TEST");
        footSensor.setSensor(true);
        footSensor.getShape().getCenter().set(0, -.5f); //Offset the fixture.
        footSensor.setUserData(FOOT_SENSOR);

        player.addFixture(footSensor);
        player.addFixture(new Rectangle(1f,1), "PLAYER_BODY");

        Body ground = new Body();
        ground.setStatic(true);
        ground.setColor(Color.GREEN);
        ground.addFixture(new Rectangle(10, 1), "GROUND");
        ground.setUserData(SOLID);

        Body wall = new Body();
        wall.setStatic(true);
        wall.addFixture(new Rectangle(1, 10), "WALL");
        wall.setUserData(SOLID);

        Gdx.graphics.setTitle("BASIC EXAMPLE");

        Body bulletExample = new Body();
        bulletExample.setStatic(false);
        bulletExample.addFixture(new Rectangle(0.5f, 0.5f));
        bulletExample.translate(3,1);
        bulletExample.setUserData(BULLET);

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
            }

            @Override
            public void end(Contact contact) {
                logger.info("CONTACT ENDED {}:{} {} {}", contact.getId(), contact.hashCode(), contact.getFixture1().getTag(), contact.getFixture2().getTag());

                if(contact.getFixture1().getUserData() == FOOT_SENSOR || contact.getFixture2().getUserData() == FOOT_SENSOR) {
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

            body.getVelocity().add(gravity.cpy().scl(delta));
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

    public class AABBCD {
        public boolean collides(Rectangle rect1, Rectangle rect2) {
            return (rect1.x < rect2.x + rect2.width && rect1.x + rect1.width > rect2.x &&
                    rect1.y < rect2.y + rect2.height && rect1.y + rect1.height > rect2.y);
        }

        public class Rectangle {
            public float x;
            public float y;
            public float width;
            public float height;
        }
    }

    public class ClippingManifoldSolver {
        public boolean getManifold(Penetration penetration, Convex convex1, Transform transform1, Convex convex2, Transform transform2, Manifold manifold) {
            manifold.clear();

            Vector2 n = penetration.normal;

//            convex1.getFarthestFeature(n, transform1);


            return false;
        }
    }

    public class Manifold {
        protected List<ManifoldPoint> points;
        protected Vector2 normal;   //Penetration normal.

        public Manifold() {
            points = new ArrayList<>(2);
        }

        public Manifold(List<ManifoldPoint> points, Vector2 normal) {
            this.points = points;
            this.normal = normal;
        }

        public void clear() {
            points.clear();
            normal = null;
        }

        public List<ManifoldPoint> getPoints() { return this.points; }
        public Vector2 getNormal() { return normal; }

        public void setPoints(List<ManifoldPoint> points) { this.points = points; }
        public void setNormal(Vector2 normal) { this.normal = normal; }
    }
    public class ManifoldPoint {
        protected final UUID id;
        private Vector2 point;
        private float depth;

        public ManifoldPoint(UUID id) {this.id = id;}
        public ManifoldPoint(UUID id, Vector2 point, float depth) {
            this.id = id;
            this.point = point;
        }

    }

    //region Listener Logic
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

    //endregion
}