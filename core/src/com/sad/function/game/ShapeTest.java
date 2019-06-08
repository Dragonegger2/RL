package com.sad.function.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.manager.ResourceManager;
import com.sad.function.physics.Physics;
import com.sad.function.physics.Ray;
import com.sad.function.physics.RayHit;
import com.sad.function.system.cd.shapes.*;
import com.sad.function.system.cd.utils.CollisionDetectionAlgorithms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

public class ShapeTest extends BaseGame {
    private static final Logger logger = LogManager.getLogger(ShapeTest.class);
    Vector2 velocity = new Vector2();

    Vector2 position;
    private OrthographicCamera camera;
    private ResourceManager resourceManager;
    private ShapeRenderer shapeRenderer;
    private Rectangle player;
    private Rectangle floor;

    private CollisionDetectionAlgorithms gjkShit;

    private List<Shape> everybody;
    private boolean isAboveSlope;

    @Override
    public void create() {
        resourceManager = new ResourceManager();

        camera = new OrthographicCamera();

        position = new Vector2();
        position.set(0, 1);
        player = new Rectangle(position, new Vector2(0.5f, 0.5f));
        floor = new Rectangle(new Vector2(0, 0), new Vector2(5f, 0.25f));

        everybody = new ArrayList<>();
        everybody.add(player);
        everybody.add(floor);

        shapeRenderer = new ShapeRenderer();
        gjkShit = new CollisionDetectionAlgorithms();
    }

    @Override
    public void render() {
        camera.position.set(position.x, position.y, 0);
        camera.update();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.add(-0.125f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.add(0.125f, 0);
        }
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            velocity.add(0, 0.125f);
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            velocity.add(0, -0.125f);
//        }

        //Kill movement if they aren't being moved.
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.sub(velocity.x, 0);//Stop moving in the xdirection if no keys are pressed.
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.sub(0, velocity.y);
        }

//        position.add(velocity.x, velocity.y);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

//        for (int i = 0; i < everybody.size(); i++) {
//            renderShape(everybody.get(i));
//        }
        renderShape(player);
        renderShape(floor);
//        renderShape(new Point(player.getLeft()));
//        renderShape(new Point(player.getRight()));
//        renderShape(new Point(player.getTop()));
        renderShape(new Point(player.getBottom()));
//        renderShape(new Point(player.getTopLeft()));
//        renderShape(new Point(player.getTopRight()));
        renderShape(new Point(player.getBottomLeft()));
        renderShape(new Point(player.getBottomRight()));

        float miny = computeLimitBottom(player, floor);

        player.getOrigin().set(player.getOrigin().x, miny + player.halfsize.y);
        renderShape(new Point(player.getBottom().add(new Vector2(0, -2))));
        renderShape(new Point(player.getBottomRight().add(new Vector2(0, -2))));
        renderShape(new Point(player.getBottomLeft().add(new Vector2(0, -2))));
        shapeRenderer.end();


        Gdx.graphics.setTitle(String.format("FPS: %s | Cam: (%s, %s)", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y));
    }

    public float computeLimitBottom(Rectangle rect, Shape s) {
        //Also need speed?
        Vector2 velocity = new Vector2();
        float rayDistance = 0.5f;//Math.abs(velocity.y) > 1 ? velocity.y : Math.signum(velocity.y) * 1;

        Ray rayBottomLeft = new Ray().setStart(rect.getBottomLeft()).cast(new Vector2(0, -1), rayDistance);
        Ray rayBottomRight = new Ray().setStart(rect.getBottomRight()).cast(new Vector2(0, -1), rayDistance);
        Ray rayBottom = new Ray().setStart(rect.getBottom()).cast(new Vector2(0, -1), rayDistance);

        Vector2 limitBottomLeft = rayBottomLeft.getEnd().cpy();
        Vector2 limitBottomRight = rayBottomRight.getEnd().cpy();
        Vector2 limitBottom = rayBottom.getEnd().cpy();

        RayHit hitBottomLeft = new RayHit();
        RayHit hitBottomRight = new RayHit();
        RayHit hitBottom = new RayHit();

        boolean slopeLeft = false;
        boolean slopeRight = false;

        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rectLine(rayBottomLeft.getStart().x, rayBottomLeft.getStart().y, rayBottomLeft.getEnd().x, rayBottomLeft.getEnd().y, 0.012f);
        shapeRenderer.rectLine(rayBottomRight.getStart().x, rayBottomRight.getStart().y, rayBottomRight.getEnd().x, rayBottomRight.getEnd().y, 0.012f);
        shapeRenderer.rectLine(rayBottom.getStart().x, rayBottom.getStart().y, rayBottom.getEnd().x, rayBottom.getEnd().y, 0.012f);

        if (Physics.rayCast(rayBottomLeft, s, hitBottomLeft)) {
            limitBottomLeft = hitBottomLeft.getCollisionPoint();
            //TODO: Not sure these are making any sense.
            slopeLeft = Math.abs(hitBottomLeft.getpNormal().angle() - 90) >= 5;
        }

        if (Physics.rayCast(rayBottomRight, s, hitBottomRight)) {
            limitBottomRight = hitBottomRight.getCollisionPoint();

            slopeRight = hitBottomRight.getpNormal().angle() - 90 >= 5;
        }

        isAboveSlope = (slopeLeft && slopeRight) ||
                (slopeLeft && !slopeRight && limitBottomLeft.y >= limitBottomRight.y) ||
                (!slopeLeft && slopeRight && limitBottomRight.y >= limitBottomLeft.y);


        if (isAboveSlope) {
            if (Physics.rayCast(rayBottom, s, hitBottom)) {
                limitBottom = hitBottom.getCollisionPoint();
            }

            if (slopeLeft && limitBottomLeft.y - limitBottom.y > 5) {
                return limitBottomLeft.y;
            } else if (slopeRight && limitBottomRight.y - limitBottom.y > 5) {
                return limitBottomRight.y;
            } else {
                return limitBottom.y;
            }
        } else {
            return Math.max(limitBottomLeft.y, limitBottomRight.y);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
    }

    @Override
    public void dispose() {
        resourceManager.dispose();
    }

    private void renderShape(Shape shape) {
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(circle.getOrigin().x, circle.getOrigin().y, circle.radius, 15);
            return;
        }
        if (shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) shape;
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(rectangle.getOrigin().x - rectangle.halfsize.x, rectangle.getOrigin().y - rectangle.halfsize.y, rectangle.halfsize.x * 2, rectangle.halfsize.y * 2);
            return;
        }
        if (shape instanceof Point) {
            Point point = (Point) shape;
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.circle(point.getOrigin().x, point.getOrigin().y, 0.0625f, 15);
            return;
        }
        if (shape instanceof Line) {
            Line line = (Line) shape;
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.rectLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y, 0.0625f);
        }
    }
}
