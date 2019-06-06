package com.sad.function.system.cd.utils;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Translation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.abs;

public class MinkowskiCollisionDetection extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(MinkowskiCollisionDetection.class);

    private ComponentMapper<PhysicsBody> mPhysics;
    private ComponentMapper<Translation> mPosition;

    private IntBag collidables;
    private Vector2 penetration = new Vector2();

    public MinkowskiCollisionDetection() {
        super(Aspect.all( Translation.class));

        collidables = new IntBag();
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void processSystem() {
        for (int a = 0; a < collidables.size(); a++) {
            for (int b = a + 1; b < collidables.size(); b++) {

                int e1 = collidables.get(a);
                int e2 = collidables.get(b);

                //WE only act on the dynamic elements.
                if(!mPhyiscs.create(e1).dynamic) { break; }

                //Are we colliding?
                if (boxesAreColliding(e1, e2, penetration)) {
                    //TODO Only two cases: 1. A single entity is non-static. 2. Both entities are non-static.
                    //Both are dynamic
                    if (mPhysics.create(e1).dynamic && mPhysics.create(e2).dynamic) {
//                        mPhysics.create(e1).getHandler().handleCollision(world, e2, penetration);
//                        mPhysics.create(e2).getHandler().handleCollision(world, e1, penetration);
                    } else { //Only one is dynamic.
                        //Figure out which one is dynamic.
//                        int dynamicEntity = mPhysics.create(e1).isStatic ? e2 : e1; //if a is static use b, otherwise just use a.
//                        int staticEntity = mPhysics.create(e1).isStatic ? e1 : e2;

//                        mPhysics.create(e1).getHandler().handleCollision(world, e2, penetration);
                        logger.info("{} collided with {}.", e1, e2);
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

    private ComponentMapper<PhysicsBody> mPhyiscs;

    private float bottom(int entity) {
        return mPhyiscs.create(entity).position.y;// + mPhysics.create(entity).yOffset;
    }

    private float top(int entity) {
        return mPhyiscs.create(entity).position.y + mPhyiscs.create(entity).getHeight();// + mPhysics.create(entity).yOffset + ;
    }

    private float left(int entity) {
        return mPhyiscs.create(entity).position.x + mPhyiscs.create(entity).getWidth() * 2;// + mPhysics.create(entity).xOffset;
    }

    private float right(int entity) {
        return mPosition.create(entity).x - mPhyiscs.create(entity).getWidth() * 2; // + mPhysics.create(entity).xOffset + mPhysics.create(entity).width;
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
