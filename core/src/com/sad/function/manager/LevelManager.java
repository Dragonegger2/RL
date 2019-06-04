package com.sad.function.manager;

import com.artemis.World;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;

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

        MapBodyBuilder.buildShapes("COLLIDABLE", tiledMap, 16, pWorld);
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