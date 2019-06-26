package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.*;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.dynamics.contact.PersistedContactPoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;


import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class ShapeTest6 extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(ShapeTest6.class);

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private Body player;
    private Body ground;

    World world = new World();

    private static final Object FLOOR_BODY = new Object();
    private static final Object PLAYER = new Object();

    private final AtomicBoolean isOnGround = new AtomicBoolean(false);

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        player = new Body();
        player.addFixture(new Rectangle(1, 1));
        player.setMass(MassType.NORMAL);
        player.translate(0, 3);
        player.translate(0,0);
        player.setUserData(PLAYER);

        ground = new Body();
        ground.addFixture(new Rectangle(100, 1));
        ground.setMass(MassType.INFINITE);
        ground.setUserData(FLOOR_BODY);


        world.addBody(player);
        world.addBody(ground);

        world.setGravity(new org.dyn4j.geometry.Vector2(0, -9.8));

        world.addListener(new StepAdapter() {
            @Override
            public void begin(Step step, World world) {
                // at the beginning of each world step, check if the body is in
                // contact with any of the floor bodies
                boolean isGround = false;
                List<Body> bodies =  player.getInContactBodies(false);
                for (int i = 0; i < bodies.size(); i++) {
                    if (bodies.get(i).getUserData() == FLOOR_BODY) {
                        isGround = true;
                        break;
                    }
                }

                if (!isGround) {
                    // if not, then set the flag, and update the color
                    isOnGround.set(false);
                }
            }
        });

        world.addListener(new ContactAdapter() {
            private boolean isContactWithFloor(ContactPoint point) {
                if ((point.getBody1().getUserData() == PLAYER || point.getBody2().getUserData() == PLAYER) &&
                        (point.getBody1().getUserData() == FLOOR_BODY || point.getBody2().getUserData() == FLOOR_BODY)) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean persist(PersistedContactPoint point) {
                if (isContactWithFloor(point)) {
                    isOnGround.set(true);
                }
                return super.persist(point);
            }

            @Override
            public boolean begin(ContactPoint point) {
                if (isContactWithFloor(point)) {
                    isOnGround.set(true);
                }
                return super.begin(point);
            }
        });
    }

    @Override
    public void render() {

        r();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        float delta = 1f / 60f;   //TODO fix my timestep.

//        //region Input Handling
        float speed = 0.125f;
        Vector2 playerSpeed = convert(player.getLinearVelocity());
//
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.applyImpulse(convert(-speed, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.applyImpulse(convert(speed, 0));
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float x = playerSpeed.x;
            if(Math.abs(x) < speed/2) {
                player.setLinearVelocity(0, player.getLinearVelocity().y);
            }
        }
        if(isOnGround.get()) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                player.applyImpulse(convert(0, 3));
            }
        }
//        endregion

        world.update(delta);

        Gdx.graphics.setTitle(String.format("FPS: %s V: %s isOnGround: %s", Gdx.graphics.getFramesPerSecond(), player.getLinearVelocity(), isOnGround.get()));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);

        if(player != null) {
            camera.position.x = (float) player.getWorldCenter().x;
            camera.position.y = (float) player.getWorldCenter().y;
        }
    }

    //region rendering logic
    public void r() {
        camera.position.set((float) player.getWorldCenter().x, (float)player.getWorldCenter().y, 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        List<Body> bodies = world.getBodies();

        shapeRenderer.setColor(Color.GRAY);
        for (int i = 0; i < bodies.size(); i++) {
            renderBody(bodies.get(i));
        }

        shapeRenderer.end();
    }

    public void renderPoint(Vector2 p) {
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.circle(p.x, p.y, 0.125f, 15);
    }

    public void renderCircle(Circle c) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(c.getCenter().x,
                c.getCenter().y,
                c.getRadius(),
                15);
    }

    public void renderRectangle(Rectangle r, Transform t) {
        Vector2 position = convert(t.getTransformed(r.getCenter()));

        float width = (float)r.getWidth();
        float height = (float)r.getHeight();

        shapeRenderer.rect(position.x - width / 2f, position.y - height / 2f, width, height);
    }

    public void renderBody(Body b) {
        Transform t = b.getTransform();
        for (int i = 0; i < b.getFixtures().size(); i++) {
            shapeRenderer.setColor(Color.FIREBRICK);
            if (b.getFixtures().get(i).getShape() instanceof Rectangle) {
                Rectangle r = (Rectangle) b.getFixtures().get(i).getShape();
                renderRectangle(r, t);
            } else {
                logger.warn("Update the RenderBody method to support additional shapes if you wish to support {}.", b.getFixtures().get(i).getClass());
            }
        }
        shapeRenderer.setColor(Color.LIME);
        renderPoint(convert(b.getWorldCenter()));
    }

    //endregion

    public static Vector2 convert(org.dyn4j.geometry.Vector2 v) {
        return new Vector2((float)v.x, (float)v.y);
    }

    public static org.dyn4j.geometry.Vector2 convert(Vector2 v) {
        return new org.dyn4j.geometry.Vector2(v.x, v.y);
    }

    public static org.dyn4j.geometry.Vector2 convert(double x, double y) {
        return new org.dyn4j.geometry.Vector2(x, y);
    }

    public static org.dyn4j.geometry.Vector2 convert(float x, float y) {
        return new org.dyn4j.geometry.Vector2(x, y);
    }

}