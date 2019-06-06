package com.sad.function.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Translation;
import com.sad.function.manager.ResourceManager;
import com.sad.function.system.cd.shapes.*;
import com.sad.function.system.cd.utils.CollisionDetectionAlgorithms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;

public class ShapeTest extends BaseGame {
    private static final Logger logger = LogManager.getLogger(ShapeTest.class);
    Vector2 velocity = new Vector2();
//    Line ray;
    Translation position;
    private OrthographicCamera camera;
    private ResourceManager resourceManager;
    private ShapeRenderer shapeRenderer;
    private Rectangle player;
    private Rectangle floor;
    private Point point;
    private Line v;

    private CollisionDetectionAlgorithms gjkShit;

    @Override
    public void create() {
        resourceManager = new ResourceManager();

        camera = new OrthographicCamera();

        //Just a reminder that infinite tile maps are never supported. It would be best for me to create one in infinite mode and then scale it down.
        //Also a reminder, all methods having to do with the map object currently return their y-values normalized for the new direction of the origin.
        position = new Translation();
        position.setX(0).setY(0);
//        player = new Circle(position, 1);
        player = new Rectangle(position, new Vector2(0.5f, 0.5f));
        floor = new Rectangle(new Translation().setX(0f).setY(0f), new Vector2(5f, 0.25f));
        shapeRenderer = new ShapeRenderer();
        point = new Point(new Translation().setX(0.5f).setY(0.5f));
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
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.add(0, 0.125f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.add(0, -0.125f);
        }

        //Kill movement if they aren't being moved.
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.sub(velocity.x, 0);//Stop moving in the xdirection if no keys are pressed.
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.sub(0, velocity.y);
        }

        position.translate(velocity.x, velocity.y, 0.0f);

        v = new Line(player.getOrigin(), player.getOrigin().cpy().add(velocity.x, velocity.y));


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        renderShape(player);
        renderShape(floor);
        renderShape(point);
        renderShape(v);

        if(gjkShit.gjk(player,floor) != null) {
            logger.info("Colliding!");
        }
        shapeRenderer.end();

        Gdx.graphics.setTitle(String.format("FPS: %s | Cam: (%s, %s)", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
    }

    @Override
    public void dispose() {
        resourceManager.dispose();
    }

    public void renderShape(Shape shape) {
        if(shape instanceof Circle) {
            Circle circle = (Circle)shape;
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(circle.getOrigin().x, circle.getOrigin().y, circle.radius, 15);
            return;
        }
        if(shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)shape;
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(rectangle.getOrigin().x - 0.5f, rectangle.getOrigin().y - 0.5f, rectangle.halfsize.x, rectangle.halfsize.y);
            return;
        }
        if(shape instanceof Point) {
            Point point = (Point)shape;
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.circle(point.getOrigin().x, point.getOrigin().y, 0.0625f, 15);
            return;
        }
        if(shape instanceof Line) {
            Line line = (Line)shape;
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.rectLine(line.getStart().x,  line.getStart().y, line.getEnd().x, line.getEnd().y, 0.0625f);
        }
    }
}
