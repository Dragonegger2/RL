package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.World;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.data.Transform;
import com.sad.function.collision.overlay.filter.Category;
import com.sad.function.collision.overlay.filter.CategoryFilter;
import com.sad.function.collision.overlay.geometry.Mass;
import com.sad.function.collision.overlay.shape.Circle;
import com.sad.function.collision.overlay.shape.Convex;
import com.sad.function.collision.overlay.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class MyWorldTest extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(MyWorldTest.class);

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private Body player;
    private Body ground;
    private Body wall;

    World world = new World();

    List<Body> bodies = new ArrayList<>(3);

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        player = new Body(1);
        Convex c = new Rectangle(1, 1);
        BodyFixture playerFixture = player.addFixture(c);
        player.translate(-3, 3);
        player.setBullet(true); //Set the body to need continuous advancement/continuous collision detection (ConservativeAdvancement/CCD)
        player.setMass(Mass.MassType.NORMAL);

        ground = new Body();
        Convex gs = new Rectangle(15f, .5f);

        BodyFixture groundFixture = ground.addFixture(gs);
        ground.translate(-10, 0);

        Convex w = new Rectangle(1, 10);
        wall = new Body(1);
        wall.addFixture(w);
        wall.translate(-10, 0);

        wall.setActive(false);
        ground.setActive(false);

        wall.setMass(Mass.MassType.INFINITE);
        ground.setMass(Mass.MassType.INFINITE);

        wall.setTag("WALL");
        ground.setTag("GROUND");
        player.setTag("PLAYER");

        world.addBody(wall);
        world.addBody(ground);
        world.addBody(player);

        CategoryFilter playerFilter = new CategoryFilter(Category.PLAYER, Category.ALL);
        CategoryFilter solid = new CategoryFilter(Category.ALL, Category.ALL);

        bodies.add(player);
        bodies.add(ground);
        bodies.add(wall);
    }

    @Override
    public void render() {
        r();

        float delta = 1f / 60f;   //TODO fix my timestep.

        //region Input Handling

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        float speed = 0.125f;
        Vector2 playerSpeed = player.getLinearVelocity();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.applyImpulse(new Vector2(-speed, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.applyImpulse(new Vector2(speed, 0));
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float x = player.getLinearVelocity().x;
            if(Math.abs(x) < speed/2) {
                player.setLinearVelocity(0, player.getLinearVelocity().y);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.applyImpulse(new Vector2(0, 12));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.applyImpulse(new Vector2(0, -speed));
        }
        //endregion


        world.update(delta);

        Gdx.graphics.setTitle(String.format("FPS: %s V: %s P:{%s, %s}", Gdx.graphics.getFramesPerSecond(), player.getLinearVelocity(),player.getWorldCenter().x, player.getWorldCenter().y));
    }

    //region rendering logic
    public void r() {
        camera.position.set(player.getWorldCenter().x, player.getWorldCenter().y, 0);
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

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
        camera.position.x = player.getWorldCenter().x;
        camera.position.y = player.getWorldCenter().y;
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
        Vector2 tOrigin = t.getTransformed(r.getCenter());

        shapeRenderer.rect(tOrigin.x - r.getWidth() / 2, tOrigin.y - r.getHeight() / 2, r.getWidth(), r.getHeight());
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
//            shapeRenderer.setColor(Color.RED);
//            renderPoint(b.getFixtures().get(i).);

        }
        shapeRenderer.setColor(Color.LIME);
        renderPoint(b.getWorldCenter());
    }

    //endregion
}