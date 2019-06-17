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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class ShapeTest3 extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(ShapeTest3.class);
    private static final float MAX_VELOCITY = 3f;
    private static final float SNAP_LIMIT = 0.5f; //TODO: I'm not sure that this is actually what this is.
    private static final Vector2 left = new Vector2(-1, 0);
    private static final Vector2 right = new Vector2(1, 0);
    private static final Vector2 up = new Vector2(0, 1);
    private static final Vector2 down = new Vector2(0, -1);
    Vector2 speed = new Vector2();
    float limitMinY,
            limitMinX,
            limitMaxY,
            limitMaxX;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private boolean isAboveSlope;

    @Override
    public void create() {

        camera = new OrthographicCamera();


        Vector2 pointA = new Vector2(1, .25f);
        Vector2 pointB = new Vector2(6, .5f);
        Vector2 pointC = new Vector2(6, .25f);

        Vector2[] vertices = new Vector2[3];
        vertices[0] = pointA;
        vertices[1] = pointB;
        vertices[2] = pointC;

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        float delta = 1f / 60f;   //TODO fix my timestep.

//        //region Input
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            speed.add(-0.125f, 0);
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            speed.add(0.125f, 0);
//        }
//        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            speed.set(0, speed.y);//Stop moving in the xdirection if no keys are pressed.
//        }

        speed.x = MathUtils.clamp(speed.x, -3f, 3f);
        speed.x = -0.125f;

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }


        //region rendering

        camera.update();

        r();


        Gdx.graphics.setTitle(String.format("FPS: %shape | Cam: (%shape, %shape) | Vel: (%shape, %shape)", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y, speed.x, speed.y));
        //endregion
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
    }

    private void r() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.end();

    }
//
//    private void renderShape(Shape shape) {
//        if (shape instanceof Circle) {
//            Circle circle = (Circle) shape;
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.circle(circle.getCenter().x, circle.getCenter().y, circle.radius, 15);
//            return;
//        }
//        if (shape instanceof Rectangle) {
//            Rectangle rectangle = (Rectangle) shape;
//            shapeRenderer.setColor(Color.GREEN);
//            shapeRenderer.rect(rectangle.getCenter().x - rectangle.halfsize.x, rectangle.getCenter().y - rectangle.halfsize.y, rectangle.halfsize.x * 2, rectangle.halfsize.y * 2);
//            return;
//        }
//        if (shape instanceof Point) {
//            Point point = (Point) shape;
//            shapeRenderer.setColor(Color.BLUE);
//            shapeRenderer.circle(point.getCenter().x, point.getCenter().y, 0.0625f, 15);
//            return;
//        }
//        if (shape instanceof Line) {
//            Line line = (Line) shape;
//            shapeRenderer.setColor(Color.PURPLE);
//            shapeRenderer.rectLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y, 0.0625f);
//            return;
//        }
//        if (shape instanceof Polygon) {
//            Polygon polygon = (Polygon) shape;
//            shapeRenderer.setColor(Color.GRAY);
//            for (int current = 0; current < polygon.getRawVertices().length - 1; current++) {
//                int next = current + 1;
//                if (next > polygon.getRawVertices().length - 1) next = 0;
//                shapeRenderer.rectLine(polygon.getRawVertices()[current].x, polygon.getRawVertices()[current].y, polygon.getRawVertices()[next].x, polygon.getRawVertices()[next].y, 0.0625f);
//
//            }
//        }
//    }
}