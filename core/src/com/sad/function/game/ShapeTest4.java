//package com.sad.function.game;
//
//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.Vector2;
//import com.sad.function.collision.differ.Collision;
//import com.sad.function.collision.differ.data.RayCollision;
//import com.sad.function.collision.differ.data.ShapeCollision;
//import com.sad.function.collision.differ.shapes.Polygon;
//import com.sad.function.collision.differ.shapes.Ray;
//import com.sad.function.collision.differ.shapes.Rectangle;
//import com.sad.function.collision.differ.shapes.Shape;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.sad.function.global.GameInfo.VIRTUAL_HEIGHT;
//
//@SuppressWarnings("ALL")
//public class ShapeTest4 extends ApplicationAdapter {
//    private static final Logger logger = LogManager.getLogger(ShapeTest4.class);
//    private final Vector2 down = new Vector2(0, -1);
//    private final Vector2 up = new Vector2(0, 1);
//    private final Vector2 left = new Vector2(-1, 0);
//    private final Vector2 right = new Vector2(1, 0);
//    private ShapeRenderer shapeRenderer;
//    private OrthographicCamera camera;
//    private Rectangle player;
//    private Polygon ground;
//    private List<Shape> collidables = new ArrayList<>();
//    private Vector2 speed;
//
//    @Override
//    public void create() {
//
//        shapeRenderer = new ShapeRenderer();
//        camera = new OrthographicCamera();
//        player = new Rectangle(0, 10, 0.5f, 0.5f, true);
//
//        ground = Polygon.rectangle(0, -0.5f, 5f, 0.5f, true);
//        collidables.add(ground);
//
//        speed = new Vector2();
//    }
//
//    @Override
//    public void render() {
//
//        float delta = 1f / 60f;   //TODO fix my timestep.
//
//        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
//            Gdx.app.exit();
//        }
//
////        speed.y += -9.8 * delta;
////        player.getOrigin().add(speed.x * delta, speed.y * delta);
//
////        List<ShapeCollision> collisions = Collision.shapeWithShapes(player, collidables, null);
////        if(!collisions.isEmpty()) {
////            logger.info("Stuff happened.");
////        }
//
//        speed.y -= 9.8 * delta;
//        player.getPosition().sub(0f, speed.y * delta);
//        r();
//
//        Gdx.graphics.setTitle(String.format("P: {%s, %s) C: {%s, %s)", player.getPosition().x,
//                player.getPosition().y,
//                camera.position.x,
//                camera.position.y));
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
//        camera.position.x = player.getPosition().x;
//        camera.position.y = player.getPosition().y;
//    }
//
//    //region rendering logic
//    public void r() {
//        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
//        camera.update();
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.LIME);
//
//
////        List<ShapeCollision> collisionList = Collision.shapeWithShapes(player, collidables, null);
//
//        renderPolygon(ground);
//        renderPolygon(player);
//
//        shapeRenderer.end();
//    }
//
//    public void renderPoint(Vector2 p) {
//        shapeRenderer.setColor(Color.PURPLE);
//        shapeRenderer.circle(p.x, p.y, 0.125f, 15);
//    }
//
////    public void renderRay(Ray r) {
////        Vector2 start = r.start;
////        Vector2 end = r.end;
////
////        if (r.infinite == Ray.InfiniteState.INFINITE) {
////            //TODO:
////        }
////        if (r.infinite == Ray.InfiniteState.INFINITE_FROM_START) {
////            end = start.cpy().add(r.getDir().cpy().scl(Float.MAX_VALUE));
////        }
////
////        shapeRenderer.rectLine(start.x, start.y, end.x, end.y, 0.125f);
////    }
//
//    public void renderPolygon(Polygon p) {
//        for (Vector2 vertex : p.transformedVertices()) {
//
//        }
//        for (int i = 0; i < p.transformedVertices().size(); i++) {
//            int j = i + 1;
//            if (j == p.transformedVertices().size()) j = 0;
//
//            shapeRenderer.rectLine(p.transformedVertices().get(i).x,
//                    p.transformedVertices().get(i).y,
//                    p.transformedVertices().get(j).x,
//                    p.transformedVertices().get(j).y, 0.0625f);
//        }
//    }
//
//    //endregion
//}