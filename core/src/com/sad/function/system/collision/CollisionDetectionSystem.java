package com.sad.function.system.collision;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
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

    private IntBag collidables;
    private Vector2 penetration = new Vector2();

    public CollisionDetectionSystem() {
        super(Aspect.all(Collidable.class, Position.class));

        collidables = new IntBag();
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void processSystem() {
        for (int a = 0; a < collidables.size(); a++) {
            for (int b = a + 1; b < collidables.size(); b++) {

                int e1 = collidables.get(a);
                int e2 = collidables.get(b);

                //Ignore static elements.
                if (mCollidable.create(e1).isStatic && mCollidable.create(e2).isStatic) {
                    break;
                }

                //Are we colliding?
                if (boxesAreColliding(e1, e2, penetration)) {
                    //TODO Only two cases: 1. A single entity is non-static. 2. Both entities are non-static.
                    //Both are dynamic
                    if (!mCollidable.create(e1).isStatic && !mCollidable.create(e2).isStatic) {
                        mCollidable.create(e1).getHandler().handleCollision(world, e2, penetration);
                        mCollidable.create(e2).getHandler().handleCollision(world, e1, penetration);
                    } else { //Only one is dynamic.
                        //Figure out which one is dynamic.
                        int dynamicEntity = mCollidable.create(e1).isStatic ? e2 : e1; //if a is static use b, otherwise just use a.
                        int staticEntity = mCollidable.create(e1).isStatic ? e1 : e2;

                        mCollidable.create(dynamicEntity).getHandler().handleCollision(world, staticEntity, penetration);
                    }
                }

            }
        }

    }

    private boolean boxesAreColliding(int a, int b, Vector2 penetration) {

        float minkowskiTop = top(a) - bottom(b);
        float minkowskiBottom = bottom(a) - top(b);
        float minkowskiLeft = left(a) - right(b);
        float minkowskiRight = right(a) - left(b);

        //If colliding calculate the penetration vector.
        if (minkowskiRight >= 0 && minkowskiLeft <= 0 && minkowskiTop >= 0 && minkowskiBottom <= 0) {
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

        penetration.set(0f, 0f);

        return false;
    }

    private float bottom(int entity) {
        return mPosition.create(entity).y + mCollidable.create(entity).yOffset;
    }

    private float top(int entity) {
        return mPosition.create(entity).y + mCollidable.create(entity).yOffset + mCollidable.create(entity).height;
    }

    private float left(int entity) {
        return mPosition.create(entity).x + mCollidable.create(entity).xOffset;
    }

    private float right(int entity) {
        return mPosition.create(entity).x + mCollidable.create(entity).xOffset + mCollidable.create(entity).width;
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
