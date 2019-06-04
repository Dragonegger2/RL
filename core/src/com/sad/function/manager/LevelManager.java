package com.sad.function.manager;

import com.artemis.World;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

/**
 * Creates and stores a reference to all objects that get created during the loading of a level.
 * <p>
 * This is going to include, but not limited to, all ECS entities (by ID) and all Box2D bodies.
 */
public class LevelManager {
    MapObjects collidables = new MapObjects();
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private World gameWorld;
    private com.badlogic.gdx.physics.box2d.World pWorld;
    private MapObjects mapObjects;
    //Number of tiles high the map is.
    private int mapHeightInTiles;
    //Total number of pixels high the map is.
    private int pixelsHigh;
    //Number of pixels per square.
    private int pixelsPerTile;

    private float ratio;

    private ArrayList<Body> levelBodies;

    public LevelManager(World gameWorld, com.badlogic.gdx.physics.box2d.World pWorld) {
        this.gameWorld = gameWorld;
        this.pWorld = pWorld;

        levelBodies = new ArrayList<>();
    }

    public void loadLevel(String levelName) {
        tiledMap = new TmxMapLoader().load(levelName);

        mapHeightInTiles = (int) tiledMap.getProperties().get("height");
        pixelsPerTile = (int) tiledMap.getProperties().get("tileheight");
        pixelsHigh = mapHeightInTiles * pixelsPerTile;

        mapObjects = tiledMap.getLayers().get("GAME_OBJECTS").getObjects();
        collidables = tiledMap.getLayers().get("COLLIDABLE").getObjects();

        ratio = (1f / pixelsPerTile);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, ratio);
        createBox2D();
    }

    private void createBox2D() {
        for (MapObject mapObject : collidables) {
            if (mapObject instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                Shape shape = getShapeFromRectangle(rectangle);

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                Body body = pWorld.createBody(bodyDef);

                Fixture fixture = body.createFixture(shape, 1f);
                fixture.setFriction(0.1f);

                //Clean up the shape when we are done.
                shape.dispose();
                Vector2 position = new Vector2();
                rectangle.getPosition(position);
                position.scl(ratio);
//                body.getPosition().set(position);

                Vector2 size = new Vector2(rectangle.width, rectangle.height);
                size.scl(ratio);
                size.scl(0.5f);
                position.add(size.x, size.y);//Offset by height and width.

                body.setTransform(position.x, position.y , 0);

                //TODO: Fix my calculations for position.

                //Keep track of all bodies we're adding to the game.
                levelBodies.add(body);
            }
        }
    }

    private Shape getShapeFromRectangle(Rectangle rectangle) {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width * 0.5f / pixelsPerTile, rectangle.height * 0.5f / pixelsPerTile);
        return polygonShape;

    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public MapObjects getMapObjects() {
        return mapObjects;
    }

    public TiledMapRenderer getTiledMapRenderer() {
        return tiledMapRenderer;
    }

}