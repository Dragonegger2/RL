package com.sad.function.system;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Collidable;
import com.sad.function.components.Position;

import java.util.*;

/**
 * Broad object collision using spacial hashing.
 * <p>
 * Logic was pulled from here: https://conkerjo.wordpress.com/2009/06/13/spatial-hashing-implementation-for-fast-2d-collisions/
 */
public class BroadSpacialManager {
    public int cellSize;
    private int _cols;
    private int _rows;
    private int sceneWidth;
    private int sceneHeight;
    private World world;

    private Map<Integer, List<Integer>> buckets;

    public BroadSpacialManager(World world) {
        buckets = new HashMap<>();
        this.world = world;
    }

    /**
     * @param sceneWidth  of the scene
     * @param sceneHeight of the scene
     * @param cellSize
     */
    public void setup(int sceneWidth, int sceneHeight, int cellSize) {
        buckets.clear();
        _cols = sceneWidth / cellSize;
        _rows = sceneHeight / cellSize;

        for (int i = 0; i < _cols * _rows; i++) {
            buckets.put(i, new ArrayList<>());
        }

        this.sceneHeight = sceneHeight;
        this.sceneWidth = sceneWidth;
        this.cellSize = cellSize;
    }

    /**
     * Clear out the buckets, not the structure itself.
     */
    public void clearBuckets() {
        for (int i = 0; i < buckets.size(); i++) {
            buckets.get(i).clear();
        }
    }

    /**
     * Add the object to the buckets for collision testing.
     *
     * @param obj
     */
    public void registerObject(int entity) {
        List<Integer> cellIds = getIdForObj(entity);

        for (int cellNumber : cellIds) {
            buckets.get(cellNumber).add(entity);
        }
    }

    /**
     * All calculations are done with the lower left corner being the origin.
     *
     * @param entity
     * @return list of buckets the current object is in.
     */
    private List<Integer> getIdForObj(int entity) {
        ArrayList<Integer> bucketsObjIsIn = new ArrayList<>();

        Vector2 min = new Vector2(
                world.getMapper(Position.class).create(entity).x,
                world.getMapper(Position.class).create(entity).y);

        Vector2 max = new Vector2(
                world.getMapper(Position.class).create(entity).x + world.getMapper(Collidable.class).create(entity).width,
                world.getMapper(Position.class).create(entity).y + world.getMapper(Collidable.class).create(entity).height);

        float width = sceneWidth / cellSize;

        //Top-Left
        addBucket(min, width, bucketsObjIsIn);
        //Top-Right
        addBucket(new Vector2(max.x, min.y), width, bucketsObjIsIn);
        //Bottom-Right
        addBucket(new Vector2(max.x, max.y), width, bucketsObjIsIn);
        //Bottom-Left
        addBucket(new Vector2(min.x, max.y), width, bucketsObjIsIn);

        return bucketsObjIsIn;
    }

    private void addBucket(Vector2 vector, float width, List<Integer> bucketToAddTo) {
        int cellPosition = (int) (
                (Math.floor(vector.x / cellSize)) +
                        (Math.floor(vector.y / cellSize)) *
                                width
        );

        if (!bucketToAddTo.contains(cellPosition))
            bucketToAddTo.add(cellPosition);
    }

    /**
     * Returns a list of nearby entities that are currently placed in the collection.
     *
     * @param obj UUID of an object managed by the EntityManager. Must have a SpriteComponent and TransformComponent (currently)
     * @return The list of objects that are colliding with the passed in obj.
     */
    public List<Integer> getNearby(int obj) {
        List<Integer> objects = new ArrayList<>();
        List<Integer> bucketIds = getIdForObj(obj);
        for (int i : bucketIds) {
            try {
                objects.addAll(buckets.get(i));
            } catch (NullPointerException e) {
                //TODO: Gotta figure out how to handle this.
//                System.out.println("Object outside of the current viewport.");
            }
        }

        return objects;
    }
}