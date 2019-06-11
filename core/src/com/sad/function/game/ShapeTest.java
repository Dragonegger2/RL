package com.sad.function.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.manager.ResourceManager;
import com.sad.function.physics.Physics;
import com.sad.function.physics.Ray;
import com.sad.function.physics.RayHit;
import com.sad.function.system.cd.shapes.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

public class ShapeTest extends BaseGame {
    private static final Logger logger = LogManager.getLogger(ShapeTest.class);
    private static final float MAX_VELOCITY = 3f;
    private static final float SNAP_LIMIT = 0.5f; //TODO: I'm not sure that this is actually what this is.
    private static final Vector2 left = new Vector2(-1, 0);
    private static final Vector2 right = new Vector2(1, 0);
    private static final Vector2 up = new Vector2(0, 1);
    private static final Vector2 down = new Vector2(0, -1);
    Vector2 velocity = new Vector2();
    private OrthographicCamera camera;
    private ResourceManager resourceManager;
    private ShapeRenderer shapeRenderer;
    private Rectangle player;
    private Rectangle floor;
    private Polygon ramp;
    private boolean isAboveSlope;
    private Ray rayBottomLeft;
    private Ray rayBottomRight;
    private Ray rayBottom = new Ray();
    private Ray rayTopLeft;
    private Ray rayTopRight;
    private Ray rayTop;
    private Ray rayLeft;
    private Ray rayRight;
    private Vector2 limitBottomLeft;
    private Vector2 limitBottomRight;
    private Vector2 limitBottom;
    private Vector2 limitTopLeft;
    private Vector2 limitTopRight;
    private Vector2 limitTop;
    private Vector2 limitLeft;
    private Vector2 limitRight;
    private RayHit hitBottomLeft;
    private RayHit hitBottomRight;
    private RayHit hitBottom = new RayHit();
    private RayHit hitTopLeft;
    private RayHit hitTopRight;
    private RayHit hitTop;
    private RayHit hitLeft;
    private RayHit hitRight;
    private List<Shape> collidables = new ArrayList<>();
    private Physics physics;
    private Line line;
    private boolean slopeRight;
    private boolean slopeLeft;

    @Override
    public void create() {
        resourceManager = new ResourceManager();

        camera = new OrthographicCamera();

        player = new Rectangle(new Vector2(0, 1), new Vector2(0.5f, 0.5f));
        floor = new Rectangle(new Vector2(0, 0), new Vector2(5f, 0.25f));

        Vector2 pointA = new Vector2(1, .25f);
        Vector2 pointB = new Vector2(6, .5f);
        Vector2 pointC = new Vector2(6, .25f);

        Vector2[] vertices = new Vector2[3];
        vertices[0] = pointA;
        vertices[1] = pointB;
        vertices[2] = pointC;

        ramp = new Polygon(new Vector2(0, 0), vertices);

        line = new Line(new Vector2(-10, 0.25f), new Vector2(1f, 0.25f));

        collidables.add(ramp);
        collidables.add(line);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        camera.position.set(player.getOrigin().x, player.getOrigin().y, 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        renderShape(player);
        renderShape(ramp);
        renderShape(line);
        if (hitBottom.getCollisionPoint() != null) {
            renderDebugRay(rayBottom, hitBottom.getCollisionPoint());
        } else {
            renderDebugRay(rayBottom, new Vector2(rayBottom.getOrigin().x, -1000));
        }
        shapeRenderer.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.add(-0.125f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.add(0.125f, 0);
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.set(0, velocity.y);//Stop moving in the xdirection if no keys are pressed.
        }

        velocity.y += -9.8f * Gdx.graphics.getDeltaTime();

        velocity.x = MathUtils.clamp(velocity.x, -MAX_VELOCITY, MAX_VELOCITY);
        velocity.y = MathUtils.clamp(velocity.y, -9.8f, 9.8f);

        player.getOrigin().add(velocity.x, 0);

        float rayDistance = player.getOrigin().y - player.getBottom().y;
        if(velocity.y < 0) {
            rayDistance += Math.abs(velocity.y);
        }

        rayBottom = new Ray().setOrigin(player.getOrigin().cpy()).setDirection(down);
        float min_Y = computeLimitBottom(player, rayDistance);

        player.getOrigin().set(player.getOrigin().x, min_Y + player.halfsize.y);
        logger.info("Minimum Y Value: {}", min_Y);
        //I only technically want to do this if my velocity is negative.
        limitBottom = player.getBottom().cpy().add(0, velocity.y);

        hitBottom = new RayHit();


        //TODO: Modify this method to accept a distance and a collision mask.
        if (Physics.rayCast(rayBottom, collidables, hitBottom, 10f)) {
//            limitBottom = hitBottom.getCollisionPoint();
            velocity.y = 0;
        }

        Gdx.graphics.setTitle(String.format("FPS: %s | Cam: (%s, %s) | Vel: (%s, %s)", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y, velocity.x, velocity.y));
    }

    public float computeLimitBottom(Rectangle rect, float rayDistance) {
        //Also need speed?
//        float rayDistance = 0.5f;//Math.abs(velocity.y) > 1 ? velocity.y : Math.signum(velocity.y) * 1;
        rayBottomLeft       = new Ray().setOrigin(rect.getLeft()).setDirection(down);
        rayBottomRight      = new Ray().setOrigin(rect.getRight()).setDirection(down);
        rayBottom           = new Ray().setOrigin(rect.getOrigin()).setDirection(down);

        //The limitsBottoms are actually origin + halfsize + velocity in our direction.
        limitBottomLeft     = new Vector2();// = rect.getBottomLeft().ad
        limitBottomRight    = new Vector2(); // = rayBottomRight.getEnd().cpy();
        limitBottom         = new Vector2(); //= rayBottom.getEnd().cpy();

        hitBottomLeft    = new RayHit();
        hitBottomRight   = new RayHit();
        hitBottom        = new RayHit();

        slopeLeft = false;
        slopeRight = false;

        if (Physics.rayCast(rayBottomLeft, collidables, hitBottomLeft, rayDistance)) {
            limitBottomLeft = hitBottomLeft.getCollisionPoint();

            //TODO: Not sure these are making any sense.
            slopeLeft = Math.abs(hitBottomLeft.getpNormal().angle() - 90) >= 5;
        }

        if (Physics.rayCast(rayBottomRight, collidables, hitBottomRight, rayDistance)) {
            limitBottomRight = hitBottomRight.getCollisionPoint();

            slopeRight = hitBottomRight.getpNormal().angle() - 90 >= 5;
        }

        isAboveSlope = (slopeLeft && slopeRight) ||
                (slopeLeft && !slopeRight && limitBottomLeft.y >= limitBottomRight.y) ||
                (!slopeLeft && slopeRight && limitBottomRight.y >= limitBottomLeft.y);


        if (isAboveSlope) {
            if (Physics.rayCast(rayBottom, collidables, hitBottom, rayDistance)) {
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

    private void renderDebugRay(Ray ray, Vector2 end) {
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rectLine(ray.getOrigin().x, ray.getOrigin().y, end.x, end.y, 0.05f);
        shapeRenderer.setColor(Color.CORAL);
        shapeRenderer.circle(ray.getOrigin().x, ray.getOrigin().y, 0.05f, 10);
        shapeRenderer.circle(end.x, end.y, 0.05f, 10);

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
            return;
        }
        if (shape instanceof Polygon) {
            Polygon polygon = (Polygon) shape;
            shapeRenderer.setColor(Color.GRAY);
            for (int current = 0; current < polygon.getVertices().length - 1; current++) {
                int next = current + 1;
                if (next > polygon.getVertices().length - 1) next = 0;
                shapeRenderer.rectLine(polygon.getVertices()[current].x, polygon.getVertices()[current].y, polygon.getVertices()[next].x, polygon.getVertices()[next].y, 0.0625f);

            }
        }
    }
}