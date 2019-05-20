package com.sad.function.system;

import com.artemis.World;
import com.badlogic.gdx.math.Rectangle;
import com.sad.function.components.Collidable;
import com.sad.function.components.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Narrow spacial manager. Collision detection using Minkowski Difference.
 */
public class NarrowSpacialManager {
    private World world;

    public NarrowSpacialManager(World world) {
        this.world = world;
    }

    /**
     *    Create a Minkowski Difference rectangle. If it contains the origin they are intersecting.
     */
    public void calculateMD(Rectangle a, Rectangle b) {
        float MDX= a.x - (b.x + b.width);
        float MDY= a.y + a.height + b.y;

        float MDW = a.width + b.width;
        float MDH = a.height + b.height;

        Rectangle md = new Rectangle(MDX,MDY, MDW, MDH);
    }

    /**
     * Returns true if the objects are colliding, otherwise it returns false.
     *
     * Calculates using Minkowski Difference.
     * @param aX
     * @param aY
     * @param aWidth
     * @param aHeight
     * @param bX
     * @param bY
     * @param bWidth
     * @param bHeight
     * @return whether the objects provided are colliding.
     */
    private boolean areCollidingUsingMD(float aX, float aY, float aWidth, float aHeight, float bX, float bY, float bWidth, float bHeight) {
        Rectangle md = new Rectangle(
                aX - (bX + bWidth),
                aY - (bY + bHeight), //Calculate the bottom, not the top like in the example AABBMD_TOP = AABBA_TOP - AABB_B_BOTTOM
                aWidth + bWidth,
                aHeight + bHeight
        );

        if(md.contains(0,0)) {
            return true;
        }
        return false;
    }

    /**
     * Accepts a broad phase collection of objects and then does refined collision detection to see if they really
     * are colliding.
     * @param listOfCollidingObjects that need to be sorted through.
     * @returns the list of objects that are actually colliding according to the Minkowski difference.
     */
    public HashMap<Integer, List<Integer>> areColliding(HashMap<Integer, List<Integer>> listOfCollidingObjects) {
        HashMap<Integer, List<Integer>> newListOfCollidingObjects = new HashMap<>();

        for(Iterator<Integer> playerObjectIterator= listOfCollidingObjects.keySet().iterator(); playerObjectIterator.hasNext();) {
            Integer entityA = playerObjectIterator.next();
            List<Integer> possibleCollisions = listOfCollidingObjects.get(entityA);
            for(Iterator<Integer> other = possibleCollisions.iterator(); other.hasNext();) {

                Integer entityB = other.next();
                if(areCollidingUsingMD(
                        world.getMapper(Position.class).create(entityA).x,
                        world.getMapper(Position.class).create(entityA).y,
                        world.getMapper(Collidable.class).create(entityA).width,
                        world.getMapper(Collidable.class).create(entityA).height,
                        world.getMapper(Position.class).create(entityB).x,
                        world.getMapper(Position.class).create(entityB).y,
                        world.getMapper(Collidable.class).create(entityB).width,
                        world.getMapper(Collidable.class).create(entityB).height
                )) {
//                    System.out.println("Objects are colliding based on minkowski principle.");
                    //If the key set does not contain this player key, add it and then register the new object.
                    if(!newListOfCollidingObjects.keySet().contains(entityA)){
                        newListOfCollidingObjects.put(entityA, new ArrayList<>());
                    }

                    newListOfCollidingObjects.get(entityA).add(entityB);
                }
            }
        }

        return newListOfCollidingObjects;
    }
}