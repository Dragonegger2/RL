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
import com.sad.function.system.cd.utils.CollisionDetectionAlgorithms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

public class ShapeTest extends BaseGame {
    private static final Logger logger = LogManager.getLogger(ShapeTest.class);
    private static final float MAX_VELOCITY = 3f;
    private static final float SNAP_LIMIT = 0.5f; //TODO: I'm not sure that this is actually what this is.
    Vector2 velocity = new Vector2();
    Vector2 position;
    private OrthographicCamera camera;
    private ResourceManager resourceManager;
    private ShapeRenderer shapeRenderer;
    private Rectangle player;
    private Rectangle floor;
    private Polygon ramp;

    private boolean isAboveSlope;

    private Ray rayBottomLeft;
    private Ray rayBottomRight;
    private Ray rayBottom;
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
    private RayHit hitBottom;
    private RayHit hitTopLeft;
    private RayHit hitTopRight;
    private RayHit hitTop;
    private RayHit hitLeft;
    private RayHit hitRight;

    private List<Shape> collidables = new ArrayList<>();

    private static final Vector2 left = new Vector2(-1, 0);
    private static final Vector2 right = new Vector2(1, 0);
    private static final Vector2 up = new Vector2(0, 1);
    private static final Vector2 down = new Vector2(0, -1);
    @Override
    public void create() {
        resourceManager = new ResourceManager();

        camera = new OrthographicCamera();

        position = new Vector2();
        position.set(0, 1);
        player = new Rectangle(position, new Vector2(0.5f, 0.5f));
        floor = new Rectangle(new Vector2(0, 0), new Vector2(5f, 0.25f));

        Vector2 pointA = new Vector2(-5,.25f);
        Vector2 pointB = new Vector2(6, .5f);
        Vector2 pointC = new Vector2(6, .25f);

        Vector2[] vertices = new Vector2[3];
        vertices[0] = pointA;
        vertices[1] = pointB;
        vertices[2] = pointC;

        ramp = new Polygon(new Vector2(0,0), vertices);

        collidables.add(floor);
        collidables.add(ramp);

        shapeRenderer = new ShapeRenderer();
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

        //Clamp X&Y Velocity.
        velocity.x = MathUtils.clamp(velocity.x, -MAX_VELOCITY, MAX_VELOCITY);

        //Kill movement if they aren't being moved.
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.set(0, velocity.y);//Stop moving in the xdirection if no keys are pressed.
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.set(velocity.x, 0);
        }

        player.getOrigin().add(velocity.x, velocity.y);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        float miny = computeLimitBottom(player, floor);
        float miny = computeLimitBottom(player, ramp);
//        float miny = computeLimitBottom(player);
        player.getOrigin().set(player.getOrigin().x, miny + player.halfsize.y);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        renderShape(player);
        renderShape(floor);

        renderShape(ramp);
        renderDebugRay(rayBottomLeft, limitBottomLeft);
        renderDebugRay(rayBottom, limitBottom);
        renderDebugRay(rayBottomRight, limitBottomRight);

        shapeRenderer.end();


        Gdx.graphics.setTitle(String.format("FPS: %s | Cam: (%s, %s) | Vel: (%s, %s)", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y, velocity.x, velocity.y));
    }

    private float computeLimitBottom(Rectangle rect) {
        float minY = Float.MAX_VALUE;
        for (Shape collidable : collidables) {
            float tmp = computeLimitBottom(rect, collidable);
            if (tmp < minY) {
                minY = tmp;
            }
        }

        return minY;
    }
    private float computeLimitBottom(Rectangle rect, Shape s) {

        //Also need speed?
        Vector2 v = new Vector2();
        //
        float rayDistance = v.y + SNAP_LIMIT;//Math.abs(velocity.y) > 1 ? velocity.y : Math.signum(velocity.y) * 1;


        rayBottomLeft = new Ray().setStart(rect.getBottomLeft()).cast(new Vector2(0, -1), rayDistance);
        rayBottomRight = new Ray().setStart(rect.getBottomRight()).cast(new Vector2(0, -1), rayDistance);
        rayBottom = new Ray().setStart(rect.getBottom()).cast(new Vector2(0, -1), rayDistance);

        //TODO Fix this not using vertical velocity.
        limitBottomLeft = rayBottomLeft.getEnd().cpy().cpy();
        limitBottomRight = rayBottomRight.getEnd().cpy();
        limitBottom = rayBottom.getEnd().cpy();

        hitBottomLeft = new RayHit();
        hitBottomRight = new RayHit();
        hitBottom = new RayHit();

        boolean slopeLeft = false;
        boolean slopeRight = false;

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

    public float computeLimitTop(Rectangle rect, Vector2 v, Shape s) {

        float m_rayDistance = v.y;

        Ray rayTopLeft = new Ray().setStart(rect.getTopLeft()).cast(new Vector2(0, 1), m_rayDistance);
        Ray rayTopRight = new Ray().setStart(rect.getTopRight()).cast(new Vector2(0, 1), m_rayDistance);

        RayHit hitTopLeft = new RayHit();
        RayHit hitTopRight = new RayHit();

        Vector2 limitTopLeft = rayTopLeft.getStart().cpy().add(0, m_rayDistance);
        Vector2 limitTopRight = rayTopRight.getStart().cpy().add(0, m_rayDistance);

        if (Physics.rayCast(rayTopLeft, s, hitTopLeft)) {
            limitTopLeft = hitTopLeft.getCollisionPoint();
        }
        if (Physics.rayCast(rayTopRight, s, hitTopRight)) {
            limitTopRight = hitTopRight.getCollisionPoint();
        }

        return Math.min(limitTopLeft.y, limitTopRight.y);
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
        shapeRenderer.rectLine(ray.getStart().x, ray.getStart().y, end.x, end.y, 0.05f);
        shapeRenderer.setColor(Color.CORAL);
        shapeRenderer.circle(ray.getStart().x, ray.getStart().y, 0.05f, 10);
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
        if(shape instanceof Polygon) {
            Polygon polygon = (Polygon)shape;
            shapeRenderer.setColor(Color.GRAY);
            for(int current = 0; current < polygon.getVertices().length - 1; current++) {
                int next = current + 1;
                if(next > polygon.getVertices().length - 1) next=0;
                shapeRenderer.rectLine(polygon.getVertices()[current].x, polygon.getVertices()[current].y, polygon.getVertices()[next].x, polygon.getVertices()[next].y, 0.0625f);

            }
        }
    }
}