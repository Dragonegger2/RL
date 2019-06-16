package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.differ.shapes.Polygon;
import com.sad.function.collision.differ.shapes.Shape;
import com.sad.function.collision.overlay.Collision;
import com.sad.function.collision.overlay.data.Penetration;
import com.sad.function.collision.overlay.shape.Circle;
import com.sad.function.collision.overlay.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

/**
 * Initial testing for Bodies, Fixtures, and the like.
 */
@SuppressWarnings("ALL")
public class ShapeTest6 extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(ShapeTest6.class);

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private List<Shape> collidables = new ArrayList<>();
    private Vector2 speed;

    private Circle player;
    private Rectangle ground;
    private Penetration p;
    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        speed = new Vector2();

        player = new Circle(new Vector2(0,0), 1);


        ground = new Rectangle(new Vector2(0,-1f), new Vector2(5, .5f), true);
    }

    @Override
    public void render() {

        float delta = 1f / 60f;   //TODO fix my timestep.

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        p = Collision.testCollision(player, ground);

        if(p != null) {
            logger.info("Collision between {} and {}", player, ground);
        }
        r();

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
        camera.position.x = player.getOrigin().x;
        camera.position.y = player.getOrigin().y;
    }


    //region rendering logic
    public void r() {
        camera.position.set(player.getOrigin().x, player.getOrigin().y, 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


        renderRectangle(ground);
        renderCircle(player);

        shapeRenderer.end();
    }

    public void renderPoint(Vector2 p) {
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.circle(p.x, p.y, 0.125f, 15);
    }

    public void renderCircle(Circle c) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(c.getOrigin().x,
                c.getOrigin().y,
                c.getRadius(),
                15);
    }

    public void renderRectangle(Rectangle r) {
        shapeRenderer.setColor(Color.FIREBRICK);
        if(r.isCentered()) {
            shapeRenderer.rect(r.getOrigin().x - r.getHalfsizeWidth(),
                    r.getOrigin().y - r.getHalfsizeHeight(),
                    r.getHalfsizeWidth()*2,
                    r.getHalfsizeHeight()*2);
        } else {
            shapeRenderer.rect(r.getOrigin().x, r.getOrigin().y,
                    r.getHalfsizeWidth()*2,
                    r.getHalfsizeHeight()*2);
        }
    }

    public void renderPolygon(Polygon p) {
        for (Vector2 vertex : p.transformedVertices()) {

        }
        for (int i = 0; i < p.transformedVertices().size(); i++) {
            int j = i + 1;
            if (j == p.transformedVertices().size()) j = 0;

            shapeRenderer.rectLine(p.transformedVertices().get(i).x,
                    p.transformedVertices().get(i).y,
                    p.transformedVertices().get(j).x,
                    p.transformedVertices().get(j).y, 0.0625f);
        }
    }

    //endregion
}