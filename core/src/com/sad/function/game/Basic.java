package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.narrowphase.GJK;
import com.sad.function.collision.overlay.shape.Convex;
import com.sad.function.collision.overlay.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;
import static java.util.Objects.hash;

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

    private Object FOOT_SENSOR = new Object();
    private Object PLAYER = new Object();
    private Object SOLID = new Object();

    public Basic() {
        player = new Body();

        player.translate(2, 1.3112774f);
        player.isStatic = false;
        player.color = Color.BLUE;
        player.tag = "PLAYER";
        player.setUserData(PLAYER);
        Fixture footSensor = new Fixture(new Rectangle(.5f, 1f), "TEST");
        footSensor.setSensor(true);
        footSensor.getShape().getCenter().set(0, -.5f); //Offset the fixture.
        footSensor.setUserData(FOOT_SENSOR);

        player.addFixture(footSensor);
        player.addFixture(new Rectangle(1f,1), "PLAYER_BODY");
    }

    @Override
    public void create() {
        gjk = new GJK();

        contactManager = new ContactManager();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();


        Body ground = new Body();
        ground.isStatic = true;
        ground.color = Color.GREEN;
        ground.tag = "GROUND";
        ground.addFixture(new Rectangle(10, 1), "GROUND");
        ground.setUserData(SOLID);

        Body wall = new Body();
        wall.isStatic = true;
        wall.tag = "WALL";
        wall.addFixture(new Rectangle(1, 10), "WALL");
        wall.setUserData(SOLID);

        Gdx.graphics.setTitle("BASIC EXAMPLE");

        bodies.add(ground);
        bodies.add(wall);
        bodies.add(player);

        //foot contact counter.
        listeners.add(new ContactAdapter() {

            @Override
            public void begin(Contact contact) {
                logger.info("NEW CONTACT {}:{} {} {}", contact.id, contact.hashCode(), contact.getFixture1().tag, contact.getFixture2().tag);
                if(contact.getFixture1().getUserData() == FOOT_SENSOR || contact.getFixture2().getUserData() == FOOT_SENSOR) {
                    footCount++;
                }
            }

            @Override
            public void end(Contact contact) {
                logger.info("CONTACT ENDED {}:{} {} {}", contact.id, contact.hashCode(), contact.getFixture1().tag, contact.getFixture2().tag);

                if(contact.getFixture1().getUserData() == FOOT_SENSOR || contact.getFixture2().getUserData() == FOOT_SENSOR) {
                    footCount--;
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


                    shapeRenderer.setColor(body.color);
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

        //add gravity.
        int size = bodies.size();
        for (int i = 0; i < size; i++) {
            Body body = bodies.get(i);
            if (body.isStatic()) continue;

            body.getVelocity().add(gravity.cpy().scl(delta));
        }

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

        Gdx.graphics.setTitle(String.format("FPS: %s  |  Velocity: %s  |  Position: %s  |  Foot Count: %s",
                Gdx.graphics.getFramesPerSecond(), player.getVelocity(), player.getPosition(), footCount));
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
                                body1.velocity.x = 0;
                            }

                            if(penetration.normal.y != 0) {
                                body1.velocity.y = 0;
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
     * Represented by a group
     */
    private class Body {
        private final UUID id;
        Color color;
        private Transform transform;
        private Transform transform0;
        private Vector2 velocity;
        private boolean isStatic = false;
        private String tag;
        private List<Fixture> fixtures;

        private Object userData;

        public Body() {
            transform = new Transform();
            velocity = new Vector2();
            color = Color.RED;
            tag = "UNSET";

            fixtures = new ArrayList<>(1);
            id = UUID.randomUUID();
        }

        public Transform getInitialTransform() {
            return transform0;
        }

        public Transform getTransform() {
            return transform;
        }

        public void translate(Vector2 t) {
            translate(t.x, t.y);
        }

        public void translate(float x, float y) {
            transform.translate(x, y);
        }

        public Vector2 getPosition() {
            return new Vector2(transform.x, transform.y);
        }

        public float getX() {
            return transform.x;
        }

        public float getY() {
            return transform.y;
        }

        public Vector2 getVelocity() {
            return velocity;
        }

        public boolean isStatic() {
            return isStatic;
        }

        public void setStatic(boolean flag) {
            isStatic = false;
        }

        //region fixture handling.

        public List<Fixture> getFixtures() {
            return fixtures;
        }
        public int getFixtureCount() {
            return fixtures.size();
        }

        public void addFixture(Fixture fixture) {
            fixtures.add(fixture);
        }

        public Fixture addFixture(Convex convex) {
            return addFixture(convex, "DEFAULT_TAG");
        }

        public Fixture addFixture(Convex convex, String fixtureTag) {
            Fixture fixture = new Fixture(convex, fixtureTag);
            fixtures.add(fixture);
            return fixture;
        }

        public boolean removeFixture(Fixture fixture) {
            return fixtures.remove(fixture);
        }

        public Fixture removeFixture(int index) {
            return fixtures.remove(index);
        }

        //endregion

        /**
         * Generated object for contacts. Used to maintain relationships between touching objects.
         */
        public void setUserData(Object userData) {
            this.userData = userData;
        }

        public Object getUserData() {
            return userData;
        }

        @Override
        public int hashCode() { return 17 * id.hashCode(); }
    }

    private class Contact {

        private final UUID id;
        private final Body body1, body2;
        private final Fixture fixture1, fixture2;
        private final Penetration penetration;

        private final boolean isSensor;

        private Contact(Body body1, Fixture fixture1, Body body2, Fixture fixture2, Penetration penetration) {
            this.id = UUID.randomUUID();
            this.body1 = body1;
            this.body2 = body2;
            this.fixture1 = fixture1;
            this.fixture2 = fixture2;
            this.penetration = penetration;

            isSensor = fixture1.isSensor() || fixture2.isSensor();
        }

        public Body getBody1() { return body1; }
        public Body getBody2() { return body2; }

        public Fixture getFixture1() { return fixture1; }
        public Fixture getFixture2() { return fixture2; }

        public UUID getId() { return id; }

        public boolean isSensor() { return isSensor; }

        @Override
        public int hashCode() {
            return hash(17,
                    body1.hashCode(),
                    fixture1.hashCode(),
                    body2.hashCode(),
                    fixture2.hashCode());
        }

        public Penetration getPenetration() {
            return penetration;
        }
    }

    /**
     * Represents a shape.
     * Has it's own UserData object, Transform (in local space), and most importantly a Convex shape.
     */
    private class Fixture {
        private final UUID id;
        private final Convex shape;
        private boolean sensor = false;
        private Object userData;
        final String tag;

        public Fixture(Convex shape) {
            this(shape, "DEFAULT_TAG");
        }

        public Fixture(Convex shape, String tag) {
            this.shape = shape;
            this.id = UUID.randomUUID();
            this.sensor = false;

            this.tag = tag;
        }

        public Convex getShape() {
            return this.shape;
        }

        public UUID getId() {
            return this.id;
        }

        public boolean isSensor() {
            return this.sensor;
        }

        public void setSensor(boolean flag) {
            this.sensor = flag;
        }

        public Object getUserData() {
            return this.userData;
        }

        public void setUserData(Object userData) {
            this.userData = userData;
        }

        @Override
        public int hashCode() {
            return 17 + id.hashCode();
        }
    }

    private class ContactManager {
        private final List<Contact> contactQueue;
        
        private Map<Integer, Contact> contacts;
        private Map<Integer, Contact> contacts1;

        public ContactManager() {
            contacts = new HashMap<>();
            contacts1 = new HashMap<>();

            contactQueue = new ArrayList<>(5);
        }

        public void addContact(Contact contact) {
            contactQueue.add(contact);
        }

        public void updateAndNotify2(List<ContactListener> listeners) {
            int size = contactQueue.size();
            int listenerSize = listeners != null ? listeners.size() : 0;

            Map<Integer, Contact> newMap = this.contacts;
            for(int i = 0; i < size; i++) {
                Contact newContact = contactQueue.get(i);

                Contact oldContact = null;
                oldContact = contacts.remove(newContact.hashCode());

                //If the old contact existed.
                if(oldContact != null) {

                }
            }

            //empty the queue when we are done.
            contactQueue.clear();
        }


        public void updateAndNotify(List<ContactListener> listeners) {
            int size = contactQueue.size();
            int listenerSize = listeners != null ? listeners.size() : 0;

            Map<Integer, Contact> newMap = this.contacts1;

            for(int i = 0; i < size; i++) {
                Contact newContact =  contactQueue.get(i);
                Contact oldContact = null;


                oldContact = contacts.remove(newContact.hashCode()); //keep removing them from contacts ensures that by the end of this we've only got those that are needed for removal at the end.

                //It's already an existing contact if it's not null.
                if(oldContact != null) {
                    for(int l = 0; l < listenerSize; l++) {
                        ContactListener listener = listeners.get(l);
                        //Oh, so it uses contact points to prevent false positives on contact. Two objects can have a persisted
                        //contact but their points AKA "Contacts" can be different. I don't have that.
                        listener.persist(oldContact);
                    }
                } else {
                    for(int l = 0; l < listenerSize; l++) {
                        ContactListener listener = listeners.get(l);
                        listener.begin(newContact);
                    }
                }
                //Either create the new contact, or put it back into the contact map.
                newMap.put(newContact.hashCode(), newContact);
            }

            //If we still have any contacts left in the map these are the contacts that need to be removed.
            if(!contacts.isEmpty()) {
                Iterator<Contact> ic = contacts.values().iterator();
                while(ic.hasNext()) {
                    Contact contact = ic.next();

                    for(int l = 0; l < listenerSize; l++) {
                        ContactListener listener = listeners.get(l);
                        listener.end(contact);
                    }

                }
            }

            //Clear the contacts and set them equal to the new map.
            if(size > 0) {
                contacts.clear();
                contacts1 = this.contacts;
                this.contacts = newMap;
            } else {
                contacts.clear();
            }

            contactQueue.clear();
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
    public interface Listener {}
    public interface StepListener extends Listener {}
    public interface ContactListener extends Listener {
        void persist(Contact contact);
        void begin(Contact contact);
        void end(Contact contact);
    }

    public class ContactAdapter implements ContactListener {

        @Override
        public void persist(Contact contact) {

        }

        @Override
        public void begin(Contact contact) {

        }

        @Override
        public void end(Contact contact) {

        }
    }

    //endregion
}