package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.Body;
import com.sad.function.collision.overlay.Collision;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.shape.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class ShapeTest5 extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(ShapeTest5.class);

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private List<Body> staticB;
    private Penetration[] penetrations;
    private Body player;
    private Body ground;
    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        player = new Body(1);
        Convex c = new Rectangle(1,1);
        player.addFixture(c);
        player.translate(5,1.4f);
        ground = new Body();
        Convex gs = new Rectangle(20, 1);
        ground.addFixture(gs);
    }

    @Override
    public void render() {

        r();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        float delta = 1f / 60f;   //TODO fix my timestep.


        float speed = 0.125f;
        Vector2 playerSpeed = player.getVelocity();
        //region Input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.getVelocity().set(-speed, playerSpeed.y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.getVelocity().set(speed, playerSpeed.y);
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.getVelocity().set(0, player.getVelocity().y);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.getVelocity().set(playerSpeed.x, speed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.getVelocity().set(playerSpeed.x, -speed);
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.getVelocity().set(playerSpeed.x, 0);
        }
        if(Collision.detect(player.getFixture(0).getShape(), player.getTransform(), ground.getFixture(0).getShape(), ground.getTransform())) {
            logger.info("Collision detected!");
        }


        player.getTransform().translate(player.getVelocity());
//        Vector2 potentialPosition = player.getCenter().add(speed.x, speed.y);


//        player.getAxes(new Transform(player.getCenter()));

        //Calculate all collisions.
//        penetrations = new Penetration[staticBodies.size()];
//        for (int i = 0; i < staticBodies.size(); i++) {
//
//            Transform t1 = new Transform(player.getCenter());
//            Transform t2 = new Transform(new Vector2(0,0));
//
//            Penetration penetration = new Penetration();
//
//            if(Collision.detect(player, t1, staticBodies.get(i), t2, penetration)) {
//                Gdx.graphics.setTitle("Collision!");
//            } else {
//                Gdx.graphics.setTitle("Not colliding.");
//            }
//        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
        camera.position.x = player.getWorldCenter().x;
        camera.position.y = player.getWorldCenter().y;
    }

    //region rendering logic
    public void r() {
        camera.position.set(player.getWorldCenter().x, player.getWorldCenter().y, 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.FIREBRICK);
        renderBody(player);
        shapeRenderer.setColor(Color.GRAY);
        renderBody(ground);

        shapeRenderer.setColor(Color.BLUE);

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
        Vector2 tOrigin = t.getTransformed(r.getCenter());

        shapeRenderer.rect(tOrigin.x - r.getWidth()/2, tOrigin.y - r.getHeight()/2, r.getWidth(), r.getHeight());
    }

    public void renderBody(Body b) {
        Transform t = b.getTransform();
        for(int i = 0; i < b.getFixtures().size(); i++) {
            if(b.getFixtures().get(i).getShape() instanceof Rectangle) {
                Rectangle r = (Rectangle)b.getFixtures().get(i).getShape();
                renderRectangle(r, t);
            } else {
                logger.warn("Update the RenderBody method to support additional shapes if you wish to support {}.", b.getFixtures().get(i).getClass());
            }
        }
    }
    //endregion
}