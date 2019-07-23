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
import com.sad.function.collision.overlay.shape.Polygon;
import com.sad.function.collision.overlay.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class Basic extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(Basic.class);
    Body player, wall, ground;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private GJK gjk;

    private Vector2 gravity = new Vector2(0, -9.8f);

    private List<Body> bodies = new ArrayList<>();

    @Override
    public void create() {
        gjk = new GJK();

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        player = new Body();
        player.translate(2, 2);
        player.isStatic = false;
        player.color = Color.BLUE;
        player.tag = "PLAYER";
        player.addFixture(new Rectangle(1,1));

        ground = new Body();
        ground.isStatic = true;
        ground.color = Color.GREEN;
        ground.tag = "GROUND";
        ground.addFixture(new Rectangle(10, 1));

        wall = new Body();
        wall.isStatic = true;
        wall.tag = "WALL";
        wall.addFixture(new Rectangle(1, 10));

        Gdx.graphics.setTitle("BASIC EXAMPLE");

        bodies.add(ground);
        bodies.add(wall);
        bodies.add(player);
    }

    @Override
    public void render() {
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

                if(f.getShape() instanceof Rectangle) {
                    Rectangle r = (Rectangle)f.getShape();

                    Vector2 position = body.getPosition().cpy();
                    position.add(f.transform.x, f.transform.y);

                    shapeRenderer.setColor(body.color);
                    shapeRenderer.rect(position.x - r.getWidth()/2, position.y - r.getHeight()/2, r.getWidth(), r.getHeight());
                }
            }
            shapeRenderer.setColor(body.color);
            shapeRenderer.rect(body.getX() - body.getShape().getWidth() / 2, body.getY() - body.getShape().getHeight() / 2, body.getShape().getWidth(), body.getShape().getHeight());
        }
        shapeRenderer.end();

        float delta = 1f / 60f;   //TODO fix my timestep.


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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.getVelocity().y = 10f;
        }


        //add gravity.
        int size = bodies.size();
        for (int i = 0; i < size; i++) {
            Body body = bodies.get(i);
            if (body.isStatic()) continue;

            body.getVelocity().add(gravity.cpy().scl(delta));
        }


        //Narrow phase. TODO: Add broadphase (like SAP).
        //TODO: Can step & check for collisions in microsteps. IE 1/10 of current step to prevent tunneling.


        int NUMBER_OF_STEPS = 10;
        float timestep = delta / NUMBER_OF_STEPS;
        for (int i = 0; i < NUMBER_OF_STEPS; i++) {
            //Move the bodies by their velocity.
            for (int j = 0; j < size; j++) {
                Body body = bodies.get(j);
                if (body.isStatic()) continue;

                body.translate(body.getVelocity().cpy().scl(timestep));
            }

            handleBodies();
        }
    }

    public void handleBodies() {
        int size = bodies.size();
        for(int i = 0; i < bodies.size(); i++) {
            //skip non-dynamic bodies
            Body body = bodies.get(i);

            if(body.isStatic()) continue;

            checkFixtureCollisions(body);
        }
    }

    public void checkFixtureCollisions(Body body1) {
        int size = bodies.size();

        for(int i = 0; i < size; i++) {
            Body body2 = bodies.get(i);

            if(body1 == body2) continue;

            int fc1 = body1.getFixtureCount();
            int fc2 = body2.getFixtureCount();

            for(int j = 0; j < fc1; j++) {
                for(int k = 0; k < fc2; k++) {

                    Fixture fixture1 = body1.getFixtures().get(j);
                    Fixture fixture2 = body2.getFixtures().get(k);


                    Penetration penetration = new Penetration();

                    if(gjk.detect(fixture1.getShape(), body1.getTransform(), fixture2.getShape(), body2.getTransform(), penetration)) {
                        if(penetration.distance == 0) {
                            logger.info("Touching.");
                            continue;
                        }

                        Vector2 translation = penetration.normal.cpy().scl(-1).scl(penetration.distance);
                        body1.translate(translation);

                        if(penetration.normal.x != 0) {
                            body1.velocity.x = 0;
                        }

                        if(penetration.normal.y != 0) {
                            body1.velocity.y = 0;
                        }
                    }
                }
            }
        }
    }

//    public void handleCollisions() {
//        int size = bodies.size();
//
//        //Check for collisions.
//        for (int i = 0; i < size; i++) {
//            Body body1 = bodies.get(i);
//            if (body1.isStatic) continue; //Only want dynamic bodies in body1.
//
//            for (int j = 0; j < size; j++) {
//                Body body2 = bodies.get(j);
//
//                if (body1 == body2) continue; //Check for identical bodies.
//
//                //TODO: Handle dynamic-dynamicc collisions.
//                if (!body1.isStatic() && !body2.isStatic()) {
//                    logger.info("BOTH ARE DYNAMIC.");
//                }
//
//                //Calculate the penetration of the two shapes.
//                Penetration penetration = new Penetration();
//                if (gjk.detect(body1.getShape(), body1.getTransform(), body2.getShape(), body2.getTransform(), penetration)) {
//
//                    if (penetration.distance == 0) continue;//Break out
//                    //NOTE: body1 is always dynamic, apply the translation only to the body.
//                    //Separate the body.
//                    Vector2 translation = penetration.normal.cpy();
//                    translation.scl(-1).scl(penetration.distance); //Had to flip the vector.
//                    body1.translate(translation);
//
//                    //If the penetration happened in the x-direction, clear the x-velocity.
//                    if (penetration.normal.x != 0) {
//                        //Collided on the side.
//                        body1.velocity.x = 0;
//                    }
//
//                    //if the penetration happened in the y-direction, clear the y-velocity.
//                    if (penetration.normal.y != 0) {
//                        body1.velocity.y = 0;
//                    }
//                }
//            }
//        }
//    }

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

        public Color color;
        private Transform transform;
        private Transform transform0;
        private Rectangle shape;
        private Vector2 velocity;
        private boolean isStatic = false;
        private String tag;
        private List<Fixture> fixtures;

        public Body() {
            transform = new Transform();
            shape = new Rectangle(0, 0);
            velocity = new Vector2();
            color = Color.RED;
            tag = "UNSET";

            fixtures = new ArrayList<>(1);
        }

        public Transform getInitialTransform() {
            return transform0;
        }

        public Transform getTransform() {
            return transform;
        }

        public Rectangle getShape() {
            return shape;
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

        public Fixture addFixture(Convex convex) {
            Fixture fixture = new Fixture(convex);
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
    }

    /**
     * Represents a shape.
     * Has it's own UserData object, Transform (in local space), and most importantly a Convex shape.
     */
    private class Fixture {
        private final UUID id;
        private final Convex shape;
        private boolean sensor;
        private Object userData;
        private Transform transform;

        public Fixture(Convex shape) {
            this.shape = shape;
            this.id = UUID.randomUUID();
            this.sensor = false;

            transform = new Transform();
            //TODO: Maybe add filtering. Would be useful, especially when I do broadphase detection.
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
            this.sensor = true;
        }

        public Object getUserData() {
            return this.userData;
        }

        public void setUserData(Object userData) {
            this.userData = userData;
        }

        //public void translateInLocalSpace()
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

    /*
            OOOH this is why he generates a contact list first, and then updates it again afterwards.

            He does it early to skip any bodies that they are in contact with.
            A contact would be what? A body that has a distance of 0 between itself and any other shapes?
            Aggregates forces.

            Applies the forces.

            Recreates contact list.

            All the while he's notifying listeners.

            This way he can notify based on beginning, ending, and persisting events for contacts. Makes it easier to manage them.
         */
}