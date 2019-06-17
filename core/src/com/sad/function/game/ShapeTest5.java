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

import java.util.ArrayList;
import java.util.List;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class ShapeTest5 extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(ShapeTest5.class);

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Vector2 speed;

    private Rectangle player;
    private Rectangle ground;
    private Rectangle wall;

    private List<Convex> staticBodies;
    private Penetration[] penetrations;
    private Body p;
    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        speed = new Vector2();

        staticBodies = new ArrayList<>();

        player = new Rectangle(1, 1);
        ground = new Rectangle(20, 1);
        wall   = new Rectangle(1, 10);

        player.translate(1, 1);

        Convex c = new Rectangle(1,1);
        p.addFixture(c);


        staticBodies.add(wall);
        staticBodies.add(ground);
    }

    @Override
    public void render() {

        r();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        float delta = 1f / 60f;   //TODO fix my timestep.


        //region Input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            speed.add(-0.125f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            speed.add(0.125f, 0);
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            speed.set(0, speed.y);//Stop moving in the xdirection if no keys are pressed.
        }

        Vector2 potentialPosition = player.getCenter().add(speed.x, speed.y);


//        player.getAxes(new Transform(player.getCenter()));

        //Calculate all collisions.
        penetrations = new Penetration[staticBodies.size()];
        for (int i = 0; i < staticBodies.size(); i++) {

            Transform t1 = new Transform(player.getCenter());
            Transform t2 = new Transform(new Vector2(0,0));

            Penetration penetration = new Penetration();

            if(Collision.detect(player, t1, staticBodies.get(i), t2, penetration)) {
                Gdx.graphics.setTitle("Collision!");
            } else {
                Gdx.graphics.setTitle("Not colliding.");
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
        camera.position.x = player.getCenter().x;
        camera.position.y = player.getCenter().y;
    }

    //region rendering logic
    public void r() {
        camera.position.set(player.getCenter().x, player.getCenter().y, 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        renderRectangle(wall);
        renderRectangle(ground);
        renderRectangle(player);

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

    public void renderRectangle(Rectangle r) {
        Vector2 tOrigin = r.getCenter();

        shapeRenderer.rect(tOrigin.x - r.getWidth()/2, tOrigin.y - r.getHeight()/2, r.getWidth(), r.getHeight());
    }
    //endregion
}