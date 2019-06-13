package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.differ.SAT2D;
import com.sad.function.collision.differ.data.RayIntersection;
import com.sad.function.collision.differ.shapes.Ray;
import com.sad.function.collision.differ.shapes.Shape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

@SuppressWarnings("ALL")
public class ShapeTest4 extends ApplicationAdapter {
    private static final Logger logger = LogManager.getLogger(ShapeTest4.class);
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    @Override
    public void create() {
        shapeRenderer  = new ShapeRenderer();
        camera = new OrthographicCamera();

    }

    @Override
    public void render() {
        camera.position.set(0,0,0);
        float delta = 1f / 60f;   //TODO fix my timestep.

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }


        Ray r1 = new Ray(new Vector2(-1, 0), new Vector2(1, 0), Ray.InfiniteState.NOT_INFINITE);
        Ray r2 = new Ray(new Vector2(0, -1), new Vector2(0, 1), Ray.InfiniteState.NOT_INFINITE);

        RayIntersection i = SAT2D.testRayVsRay(r1, r2, null);

        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.LIME);
        renderRay(r1);
        renderRay(r2);

        if(i != null) {
            renderPoint(i.getIntersectionPoint());
        }
        shapeRenderer.end();
        //endregion
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
    }

    public void renderPoint(Vector2 p) {
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.circle(p.x, p.y, 0.125f, 15);
    }
    public void renderRay(Ray r) {
        Vector2 start = r.start;
        Vector2 end = r.end;

        if(r.infinite == Ray.InfiniteState.INFINITE) {
            //TODO:
        }
        if(r.infinite == Ray.InfiniteState.INFINITE_FROM_START) {
            end = start.cpy().add(r.getDirection().cpy().scl(Float.MAX_VALUE));
        }

        shapeRenderer.rectLine(start.x, start.y, end.x, end.y, 0.125f);
    }

//
//    private void renderShape(Shape shape) {
//        if (shape instanceof Circle) {
//            Circle circle = (Circle) shape;
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.circle(circle.getOrigin().x, circle.getOrigin().y, circle.radius, 15);
//            return;
//        }
//        if (shape instanceof Rectangle) {
//            Rectangle rectangle = (Rectangle) shape;
//            shapeRenderer.setColor(Color.GREEN);
//            shapeRenderer.rect(rectangle.getOrigin().x - rectangle.halfsize.x, rectangle.getOrigin().y - rectangle.halfsize.y, rectangle.halfsize.x * 2, rectangle.halfsize.y * 2);
//            return;
//        }
//        if (shape instanceof Point) {
//            Point point = (Point) shape;
//            shapeRenderer.setColor(Color.BLUE);
//            shapeRenderer.circle(point.getOrigin().x, point.getOrigin().y, 0.0625f, 15);
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
//            for (int current = 0; current < polygon.getVertices().length - 1; current++) {
//                int next = current + 1;
//                if (next > polygon.getVertices().length - 1) next = 0;
//                shapeRenderer.rectLine(polygon.getVertices()[current].x, polygon.getVertices()[current].y, polygon.getVertices()[next].x, polygon.getVertices()[next].y, 0.0625f);
//
//            }
//        }
//    }
}