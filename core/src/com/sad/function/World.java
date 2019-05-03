package com.sad.function;

/**
 * Represents the over world and all generated dungeons & cities.
 */
public class World {
    private Tile[][] worldMap;

    public World(Tile[][] worldMap) {
        this.worldMap = worldMap;
    }

    public Tile[][] getWorldMap() { return worldMap; }
}
