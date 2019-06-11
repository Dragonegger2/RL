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

@SuppressWarnings("ALL")
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

    private boolean slopeRight;
    private boolean slopeLeft;
    private RayHit hitLeftTop;
    private RayHit hitLeftBottom;
    private RayHit hitRightTop;
    private RayHit hitRightBottom;

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

        Line line = new Line(new Vector2(-10, 0.25f), new Vector2(1f, 0.25f));
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

        for(Shape s : collidables) {
            renderShape(s);
        }

        renderShape(player);
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

        //Apply gravity.
        velocity.y += -9.8f * Gdx.graphics.getDeltaTime();

        //Clamp my velocity values.
        velocity.x = MathUtils.clamp(velocity.x, -MAX_VELOCITY, MAX_VELOCITY);
        velocity.y = MathUtils.clamp(velocity.y, -9.8f, 9.8f);

        player.getOrigin().add(velocity.x, 0);

        //All we're using this rayDistanace for is to find the closest point. Later, we're going to take into account our speed and then check if our position would exceed the minimum position.
        float rayDistance = 200f;

        //This is the closest point in the negative y direction.
        float limitMinY = computeLimitBottom(player, rayDistance);

//        player.getOrigin().set(player.getOrigin().x, limitMinY + player.halfsize.y);

        logger.info("Minimum Y Value: {}", limitMinY);

        //I only technically want to do this if my velocity is negative.
//        limitBottom = player.getBottom().cpy().add(0, velocity.y);
//
//        hitBottom = new RayHit();
//
//
//        //TODO: Modify this method to accept a distance and a collision mask.
//        if (Physics.rayCast(rayBottom, collidables, hitBottom, 10f)) {
////            limitBottom = hitBottom.getCollisionPoint();
//            velocity.y = 0;
//        }

        Gdx.graphics.setTitle(String.format("FPS: %s | Cam: (%s, %s) | Vel: (%s, %s)", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y, velocity.x, velocity.y));
    }

    private float computeLimitBottom(Rectangle rect, float rayDistance) {
        rayBottomLeft       = new Ray().setOrigin(rect.getLeft()).setDirection(down);
        rayBottomRight      = new Ray().setOrigin(rect.getRight()).setDirection(down);
        rayBottom           = new Ray().setOrigin(rect.getOrigin()).setDirection(down);

        //The limitsBottoms are actually origin + halfsize + velocity in our direction.
        //TODO: I need to figure out what the below snippet is trying to do:
        /*
         * Vector3 limitBottomLeft = rayBottomLeft.origin + Vector3.down * m_rayDistance;
         * Vector3 limitBottomRight = rayBottomRight.origin + Vector3.down * m_rayDistance;
         * Vector3 limitBottom = rayBottom.origin + Vector3.down * m_rayDistance;
         */
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

            //TODO: Not sure these are making any sense...
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

    /**
     * Calculates the farthest an entity can move. Doesn't matter if the entity can't move that far.
     * @param rect
     * @param rayDistance
     * @return
     */
    private float computeLimitTop(Rectangle rect, float rayDistance) {
        rayTopLeft = new Ray().setOrigin(rect.getTopLeft()).setDirection(up);
        rayTopRight = new Ray().setOrigin(rect.getTopRight()).setDirection(up);


        limitTopLeft = rayTopLeft.getOrigin().cpy().add(up.scl(rayDistance));
        limitTopRight = rayTopRight.getOrigin().cpy().add(up.scl(rayDistance));

        if(Physics.rayCast(rayTopLeft, collidables, hitTopLeft, rayDistance)) {
            limitTopLeft = hitTopLeft.getCollisionPoint();
        }

        if(Physics.rayCast(rayTopRight, collidables, hitTopRight, rayDistance)) {
            limitTopRight = hitTopRight.getCollisionPoint();
        }

        return Math.min(limitTopLeft.y, limitTopRight.y);
    }

    //TODO Need to ensure that this is accurate.
    private float slopeLimitAngle = 5;

    private float computeLimitLeft(Rectangle rect, float rayDistance) {
        //Don't measure from the edge you're checking to prevent issues arising from penetration.
        Ray rayLeft = new Ray()
                .setOrigin(rect.getRight())
                .setDirection(left);

        Ray rayLeftTop = new Ray()
                .setOrigin(rect.getRight().x, rect.getTop().y)
                .setDirection(left);

        Ray rayLeftBottom = new Ray()
                .setOrigin(rect.getRight().x, rect.getBottom().y)
                .setDirection(left);

        Vector2 limitLeft = rayLeft.getOrigin().cpy().add(left.scl(rayDistance));
        Vector2 limitLeftTop = rayLeftTop.getOrigin().cpy().add(left.scl(rayDistance));
        Vector2 limitLeftBottom = rayLeftBottom.getOrigin().cpy().add(left.scl(rayDistance));

        if(Physics.rayCast(rayLeft, collidables, hitLeft, rayDistance)) {
            if(hitLeft.getpNormal().angle() > slopeLimitAngle)
                limitLeft = hitLeft.getCollisionPoint();
        }
        
        if(Physics.rayCast(rayTopLeft, collidables, hitLeftTop, rayDistance)) {
            if(hitLeftTop.getpNormal().angle() > slopeLimitAngle) {
                limitLeftTop = hitLeftTop.getCollisionPoint();
            }
        }
        
        if(Physics.rayCast(rayLeftBottom, collidables, hitLeftBottom, rayDistance)) {
            if(hitLeftBottom.getpNormal().angle() > slopeLimitAngle) {
                limitLeftBottom = hitLeftBottom.getCollisionPoint();
            }
        }

        return Math.max(Math.max(limitLeft.x, limitLeftTop.x), limitLeftBottom.x);
    }

    private float computeLimitRight(Rectangle rect, float rayDistance) {
        Ray rayRight = new Ray().setOrigin(rect.getLeft()).setDirection(right);
        Ray rayRightTop = new Ray().setOrigin(rect.getLeft()).setDirection(right);
        Ray rayRightBottom = new Ray().setOrigin(rect.getLeft()).setDirection(right);

        Vector2 limitRight = rayRight.getOrigin().cpy().add(right.scl(rayDistance));
        Vector2 limitRightTop = rayRightTop.getOrigin().cpy().add(right.scl(rayDistance));
        Vector2 limitRightBottom = rayRightBottom.getOrigin().cpy().add(right.scl(rayDistance));

        if(Physics.rayCast(rayRight, collidables, hitRight, rayDistance)) {
            if(hitRight.getpNormal().angle()> slopeLimitAngle) {
                limitRight = hitRight.getCollisionPoint();
            }
        }

        if(Physics.rayCast(rayRightTop, collidables, hitRightTop, rayDistance)) {
            if(hitRightTop.getpNormal().angle() > slopeLimitAngle) {
                limitRightTop = hitRightTop.getCollisionPoint();
            }
        }

        if(Physics.rayCast(rayRightBottom, collidables, hitRightBottom, rayDistance)) {
            if(hitRightBottom.getpNormal().angle() > slopeLimitAngle) {
                limitRightBottom = hitRightBottom.getCollisionPoint();
            }
        }

        return Math.min(Math.min(limitRight.x, limitRightTop.x), limitRightBottom.x);
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