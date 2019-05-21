package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Collidable;
import com.sad.function.components.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.abs;

public class CollisionDetectionSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(CollisionDetectionSystem.class);

    private ComponentMapper<Collidable> mCollidable;
    private ComponentMapper<Position> mPosition;

    //TODO: Add broad phase spacial management.
    private NarrowSpacialManager narrowSpacialManager;

    private IntBag collidables;
    private IntBag statics;

    public CollisionDetectionSystem() {
        super(Aspect.all(Collidable.class, Position.class));

        narrowSpacialManager = new NarrowSpacialManager();

        collidables = new IntBag();
        statics = new IntBag();
    }
    private static final Vector2 origin = new Vector2(0.0f, 0.0f);
    Vector2 penetration = new Vector2();

    @Override
    protected void processSystem() {
        for (int a = 0; a < collidables.size(); a++) {
            for (int b = a + 1; b < collidables.size(); b++) {

                //Skip if both entities are "sleeping."
                if(areBoxesColliding(a, b, penetration)) {
                    //Apply the penetration?
                    logger.info("Penetration: {}", penetration);
                    //Figure out who, if anybody is static.
                    //TODO Only two cases: 1. A single entity is non-static. 2. Both entities are non-static.
                    boolean bothAreDynamic = !mCollidable.create(a).isStatic && !mCollidable.create(b).isStatic;

                    if(bothAreDynamic) {
                        logger.info("Both entities were dynamic. Needs a case to be written for it.");
                    } else {
                        int dynamic = mCollidable.create(a).isStatic ? b : a; //if a is static use b, otherwise just use a.

                        mPosition.create(dynamic).x += penetration.x;
                        mPosition.create(dynamic).y += penetration.y;
                    }
                }

            }
        }

    }

    private boolean areBoxesColliding(int a, int b, Vector2 penetration) {

        float minkowskiTop = top(a) - bottom(b);
        float minkowskiBottom = bottom(a) - top(b);
        float minkowskiLeft = left(a) - right(b);
        float minkowskiRight = right(a) - left(b);

        if(minkowskiRight >= 0 && minkowskiLeft <= 0 && minkowskiTop >= 0 && minkowskiBottom <= 0) {
            float min = Float.MAX_VALUE;

            if (abs(minkowskiLeft) < min) {
                min = abs(minkowskiLeft);
                penetration.set(minkowskiLeft, 0.0f);
            }
            if (abs(minkowskiRight) < min) {
                min = abs(minkowskiRight);
                penetration.set(minkowskiRight, 0.0f);
            }
            if (abs(minkowskiTop) < min) {
                min = abs(minkowskiTop);
                penetration.set(0.0f, minkowskiTop);
            }
            if (abs(minkowskiBottom) < min) {
                penetration.set(0.0f, minkowskiBottom);
            }

            return true;
        }

        penetration.set(0f,0f);

        return false;
    }

    private float bottom(int entity) {
        return mPosition.create(entity).y;
    }

    private float top(int entity) {
        return mPosition.create(entity).y + mCollidable.create(entity).height;
    }

    private float left(int entity) {
        return mPosition.create(entity).x;
    }

    private float right(int entity) {
        return mPosition.create(entity).x + mCollidable.create(entity).width;
    }

    private Rectangle check(int a, int b) {
        return narrowSpacialManager.minkowskiDifference(
                world.getMapper(Position.class).create(a).x,
                world.getMapper(Position.class).create(a).y,
                world.getMapper(Collidable.class).create(a).width,
                world.getMapper(Collidable.class).create(a).height,
                world.getMapper(Position.class).create(b).x,
                world.getMapper(Position.class).create(b).y,
                world.getMapper(Collidable.class).create(b).width,
                world.getMapper(Collidable.class).create(b).height
        );
    }

    private int whoIsAwake(int a, int b) {
        boolean aIsAwake = mCollidable.create(a).isStatic;
        boolean bIsAwake = mCollidable.create(b).isStatic;

        //Both are isStatic
        if (bIsAwake && aIsAwake) {
            return a + b + 1;           //"Random" offset to prevent error when an entity has an id of 0.
        }
        //A is isStatic
        if (aIsAwake) {
            return a;
        }
        //B is isStatic
        if (bIsAwake) {
            return b;
        }

        //Nobody is isStatic.
        return -1;
    }

    public boolean detect() {

        return false;
    }

    @Override
    public void inserted(int entity) {
        collidables.add(entity);
    }

    @Override
    public void removed(int entity) {
        if (collidables.indexOf(entity) != -1) {
            collidables.remove(collidables.indexOf(entity));
        }
    }
}
