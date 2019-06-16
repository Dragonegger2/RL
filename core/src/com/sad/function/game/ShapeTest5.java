package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.overlay.Collision;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.shape.Circle;
import com.sad.function.collision.overlay.shape.Rectangle;
import com.sad.function.collision.overlay.shape.Shape;
import com.sad.function.collision.overlay.shape.Transform;
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

    private List<Shape> staticBodies;
    private Penetration[] penetrations;

    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        speed = new Vector2();

        staticBodies = new ArrayList<>();

        player = new Rectangle(new Vector2(3, 0), new Vector2(0.5f, 0.5f), true);
        ground = new Rectangle(new Vector2(-2, -1f), new Vector2(10, .5f), true);
        ground = new Rectangle(new Vector2(-2, -1f), new Vector2(10, .5f), true);
        wall = new Rectangle(new Vector2(-2, -1f), new Vector2(1, 10), true);
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
        float minTranslationDistance = Float.MAX_VALUE;
        Vector2 direction = new Vector2();
        penetrations = new Penetration[staticBodies.size()];
        for (int i = 0; i < staticBodies.size(); i++) {
            //Naive way of doing this I beleive
            Transform t1 = new Transform(player.getCenter());
            Transform t2 = new Transform(staticBodies.get(i).getCenter());

            Penetration penetration = new Penetration();

            if(Collision.detect(player, t1, staticBodies.get(i), t2, penetration)) {
                Gdx.graphics.setTitle("Collision!");
                player.getCenter().add(penetration.normal.scl(penetration.distance));
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
        shapeRenderer.setColor(Color.FIREBRICK);
        if (r.isCentered()) {
            shapeRenderer.rect(r.getCenter().x - r.getHalfsizeWidth(),
                    r.getCenter().y - r.getHalfsizeHeight(),
                    r.getHalfsizeWidth() * 2,
                    r.getHalfsizeHeight() * 2);
        } else {
            shapeRenderer.rect(r.getCenter().x, r.getCenter().y,
                    r.getHalfsizeWidth() * 2,
                    r.getHalfsizeHeight() * 2);
        }
    }
    //endregion
}