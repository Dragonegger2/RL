//package com.sad.function.game;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.math.Vector2;
//import com.sad.function.global.GameInfo;
//import com.sad.function.manager.ResourceManager;
//import com.sad.function.physics.Physics;
//import com.sad.function.physics.Ray;
//import com.sad.function.physics.RayHit;
//import com.sad.function.system.cd.shapes.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;
//
//@SuppressWarnings("ALL")
//public class ShapeTest2 extends BaseGame {
//    private static final Logger logger = LogManager.getLogger(ShapeTest2.class);
//    private static final float MAX_VELOCITY = 3f;
//    private static final float SNAP_LIMIT = 0.5f; //TODO: I'm not sure that this is actually what this is.
//    private static final Vector2 left = new Vector2(-1, 0);
//    private static final Vector2 right = new Vector2(1, 0);
//    private static final Vector2 up = new Vector2(0, 1);
//    private static final Vector2 down = new Vector2(0, -1);
//    Vector2 speed = new Vector2();
//    float limitMinY,
//            limitMinX,
//            limitMaxY,
//            limitMaxX;
//
//    private OrthographicCamera camera;
//    private ResourceManager resourceManager;
//    private ShapeRenderer shapeRenderer;
//    private Rectangle player;
//    private Rectangle floor;
//    private Polygon ramp;
//    private boolean isAboveSlope;
//    private Ray rayBottomLeft;
//    private Ray rayBottomRight;
//    private Ray rayBottom = new Ray();
//    private Ray rayTopLeft;
//    private Ray rayTopRight;
//    private Ray rayTop;
//    private Ray rayLeft;
//    private Ray rayRight;
//    private RayHit hitBottomLeft = new RayHit();
//    private RayHit hitBottomRight = new RayHit();
//    private RayHit hitBottom = new RayHit();
//    private RayHit hitTopLeft = new RayHit();
//    private RayHit hitTopRight = new RayHit();
//    private RayHit hitTop = new RayHit();
//    private RayHit hitLeft = new RayHit();
//    private RayHit hitRight = new RayHit();
//    private RayHit hitLeftTop = new RayHit();
//    private RayHit hitLeftBottom = new RayHit();
//    private RayHit hitRightTop = new RayHit();
//    private RayHit hitRightBottom = new RayHit();
//    private List<Shape> collidables = new ArrayList<>();
//    private Physics physics;
//    private boolean slopeRight;
//    private boolean slopeLeft;
//
//    //TODO Need to ensure that this is accurate.
//    private float slopeLimitAngle = 5;
//    private boolean isJumping;
//    private boolean isOnSlope;
//    private boolean isRunning;
//    private boolean isGrounded;
//
//    private List<Shape> debugRenderShapes = new ArrayList<>();
//    private Ray rayRightTop;
//    private Ray rayRightBottom;
//    private Ray rayLeftTop;
//    private Ray rayLeftBottom;
//    private boolean isTopBlocked;
//    private boolean isLeftBlocked;
//    private boolean isRightBlocked;
//
//    @Override
//    public void create() {
//        resourceManager = new ResourceManager();
//
//        camera = new OrthographicCamera();
//
//        player = new Rectangle(new Vector2(0, 1), new Vector2(0.5f, 0.5f));
//        floor = new Rectangle(new Vector2(0, 0), new Vector2(5f, 0.25f));
//
//        Vector2 pointA = new Vector2(1, .25f);
//        Vector2 pointB = new Vector2(6, .5f);
//        Vector2 pointC = new Vector2(6, .25f);
//
//        Vector2[] vertices = new Vector2[3];
//        vertices[0] = pointA;
//        vertices[1] = pointB;
//        vertices[2] = pointC;
//
//        ramp = new Polygon(new Vector2(0, 0), vertices);
//
//        Line line = new Line(new Vector2(-10, 0.25f), new Vector2(1f, 0.25f));
//        Line l2 = new Line(new Vector2(-10, -10), new Vector2(-10, 10));
//        collidables.add(ramp);
//        collidables.add(line);
//        collidables.add(l2);
//        shapeRenderer = new ShapeRenderer();
//    }
//
//    @Override
//    public void render() {
//        float delta = 1f / 60f;   //TODO fix my timestep.
//
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
//
//        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
//            Gdx.app.exit();
//        }
//
//        //endregion
//
//        speed.x = MathUtils.clamp(speed.x, -5, 5);
////        speed.y += -9.8f * delta;
//
//
//        player.getOrigin().set(player.getOrigin().x + speed.x, player.getOrigin().y);
//
//        float rayDistance = player.halfsize.y + 0.125f;
//        if (speed.y < 0) {
//            rayDistance += Math.abs(speed.y);
//        }
//        float minimumY = getYMinimum(rayDistance);
//
//        if (speed.x < 0) {
//            rayDistance = player.halfsize.x + Math.abs(speed.x);
//            limitMinX = computeLimitLeft(rayDistance);
//        }
//        if (speed.x > 0) {
//            rayDistance = player.halfsize.x + Math.abs(speed.x);
//            limitMaxX = computeLimitRight(rayDistance);
//        }
//
//        float posMinY = minimumY + player.halfsize.y;
//        float posMaxX = limitMaxX - player.halfsize.x;
//        float posMinX = limitMinX + player.halfsize.x;
//        float posMaxY = Float.POSITIVE_INFINITY;
//        //TODO Top
//
//        isGrounded = player.getBottom() <= minimumY;
//        isTopBlocked = player.getTop() >= limitMaxY + 0.0125f;
//        isLeftBlocked = player.getLeft() <= limitMinX + 0.0125f;
//        isRightBlocked = player.getRight() >= limitMaxX - 0.0125f;
//
//        if (isGrounded && !isJumping) {
//            speed.y = 0;
//            isOnSlope = isAboveSlope;
//        } else {
//            speed.y = Math.max(speed.y - GameInfo.GRAVITY * delta, -GameInfo.MAX_FALL_SPEED);
//            isOnSlope = false;
//        }
//
//        if ((isLeftBlocked && speed.x < 0) || (isRightBlocked && speed.x > 0)) {
//            speed.x = 0;
//        }
//
//        if (isJumping) {
//            if (isTopBlocked) {
//                speed.y = 0;
//            }
//
//            if (speed.y <= 0)
//                isJumping = false;
//        }
//
//        if (isOnSlope) {
//            player.getOrigin().set(player.getOrigin().x, posMinY);
//        } else {
//            player.getOrigin().set(player.getOrigin().x,
//                    MathUtils.clamp(player.getOrigin().y + speed.y * delta, posMinY, posMaxY));
//        }
//
//        player.getOrigin().set(MathUtils.clamp(player.getOrigin().x + speed.x * delta, posMinX, posMaxX), player.getOrigin().y);
//
//        //region rendering
//
//        camera.position.set(player.getOrigin().x, player.getOrigin().y, 0);
//        camera.update();
//
//        r();
//
//
//        Gdx.graphics.setTitle(String.format("FPS: %s | Cam: (%s, %s) | Vel: (%s, %s)", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y, speed.x, speed.y));
//        //endregion
//    }
//
//
//    //TODO: Reimplement this to capture collision data and return that, instead of returning a float.
//
//    /**
//     * @param rayDistance calculated maximum distance that the object could move in the frame.
//     * @return the minimum y position the object can move to.
//     */
//    public float getYMinimum(float rayDistance) {
//        float maximumMovementDownward = rayDistance;
//
//        rayBottom = new Ray().setOrigin(player.getOrigin()).setDirection(down);
//        rayBottomLeft = new Ray().setOrigin(player.getLeft(), player.getOrigin().y).setDirection(down);
//        rayBottomRight = new Ray().setOrigin(player.getRight(), player.getOrigin().y).setDirection(down);
//
//        Vector2 limitBottom = rayBottom.getOrigin().cpy().add(down.cpy().scl(rayDistance));
//        Vector2 limitBottomRight = rayBottomRight.getOrigin().cpy().add(down.cpy().scl(rayDistance));
//        Vector2 limitBottomLeft = rayBottomLeft.getOrigin().cpy().add(down.cpy().scl(rayDistance));
//
//
//        if (Physics.rayCast(rayBottomLeft, collidables, hitBottomLeft, rayDistance)) {
//            logger.info("Hit bottom left.");
//            limitBottomLeft = hitBottomLeft.getCollisionPoint();
//            slopeLeft = hitBottomLeft.getpNormal().angle() - 90 >= 5; //TODO Fine tune this.
//        }
//
//        if (Physics.rayCast(rayBottomRight, collidables, hitBottomRight, rayDistance)) {
//            logger.info("Hit bottom right.");
//            limitBottomRight = hitBottomRight.getCollisionPoint();
//            slopeRight = hitBottomLeft.getpNormal().angle() - 90 >= 5; //TODO Fine tune this.
//        }
//
//        isAboveSlope = (slopeLeft && slopeRight) ||
//                (slopeLeft && !slopeRight && limitBottomLeft.y >= limitBottomRight.y) ||
//                (!slopeLeft && slopeRight && limitBottomRight.y >= limitBottomLeft.y);
//
//        if (isAboveSlope) {
//            if (Physics.rayCast(rayBottom, collidables, hitBottom, rayDistance)) {
//                logger.info("Hit bottom center.");
//                limitBottom = hitBottom.getCollisionPoint();
//            }
//
//            if (slopeLeft && limitBottomLeft.y - limitBottom.y > 5) //Again, this arbitrary number.
//                return limitBottomLeft.y;
//            else if (slopeRight && limitBottomRight.y - limitBottom.y > 5)
//                return limitBottomRight.y;
//            else
//                return limitBottom.y;
//
//        } else {
//            return Math.max(limitBottomLeft.y, limitBottomRight.y);
//        }
//    }
//
//    public float computeLimitRight(float rayDistance) {
//        rayRight = new Ray().setOrigin(player.getOrigin()).setDirection(right);
//        rayRightTop = new Ray().setOrigin(player.getOrigin().x, player.getTop()).setDirection(right);
//        rayRightBottom = new Ray().setOrigin(player.getOrigin().x, player.getBottom()).setDirection(right);
//
//        Vector2 limitRight = rayRight.cast(rayDistance);
//        Vector2 limitRightTop = rayRightTop.cast(rayDistance);
//        Vector2 limitRightBottom = rayRightBottom.cast(rayDistance);
//
//        if (Physics.rayCast(rayRight, collidables, hitRight, rayDistance)) {
//            limitRight = hitRight.getCollisionPoint();
//        }
//        if (Physics.rayCast(rayRightTop, collidables, hitRightTop, rayDistance)) {
//            limitRightTop = hitRightTop.getCollisionPoint();
//        }
//        if (Physics.rayCast(rayRightBottom, collidables, hitRightBottom, rayDistance)) {
//            limitRightBottom = hitRightBottom.getCollisionPoint();
//        }
//
//        return Math.max(Math.max(limitRight.x, limitRightBottom.x), limitRightTop.x);
//    }
//
//    public float computeLimitLeft(float rayDistance) {
//        rayLeft = new Ray().setOrigin(player.getOrigin()).setDirection(left);
//        rayLeftTop = new Ray().setOrigin(player.getOrigin().x, player.getTop()).setDirection(left);
//        rayLeftBottom = new Ray().setOrigin(player.getOrigin().x, player.getBottom()).setDirection(left);
//
//        Vector2 limitLeft = rayLeft.cast(rayDistance);
//        Vector2 limitLeftTop = rayLeftTop.cast(rayDistance);
//        Vector2 limitLeftBottom = rayLeftBottom.cast(rayDistance);
//
//        if (Physics.rayCast(rayLeft, collidables, hitLeft, rayDistance)) {
//            limitLeft = hitLeft.getCollisionPoint();
//        }
//        if (Physics.rayCast(rayLeftTop, collidables, hitLeftTop, rayDistance)) {
//            limitLeftTop = hitLeftTop.getCollisionPoint();
//        }
//        if (Physics.rayCast(rayLeftTop, collidables, hitLeftBottom, rayDistance)) {
//            limitLeftBottom = hitLeftBottom.getCollisionPoint();
//        }
//
//        return Math.max(Math.max(limitLeft.x, limitLeftBottom.x), limitLeftTop.x);
//    }
//
//    public void addRayDebugRenderShape(Vector2 a, Vector2 b) {
//        debugRenderShapes.add(new Line(a, b));
//        debugRenderShapes.add(new Point(a));
//        debugRenderShapes.add(new Point(b));
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
//    }
//
//    @Override
//    public void dispose() {
//        resourceManager.dispose();
//    }
//
//    private void renderDebugRay(Ray ray, Vector2 end) {
//        shapeRenderer.setColor(Color.FIREBRICK);
//        shapeRenderer.rectLine(ray.getOrigin().x, ray.getOrigin().y, end.x, end.y, 0.05f);
//        shapeRenderer.setColor(Color.CORAL);
//        shapeRenderer.circle(ray.getOrigin().x, ray.getOrigin().y, 0.05f, 10);
//        shapeRenderer.circle(end.x, end.y, 0.05f, 10);
//    }
//
//    private void r() {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//        for (Shape s : collidables) {
//            renderShape(s);
//        }
//
//        renderShape(player);
//
//        for (int i = 0; i < debugRenderShapes.size(); i++) {
//            renderShape(debugRenderShapes.remove(i));
//        }
//
//        shapeRenderer.end();
//
//    }
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
//}