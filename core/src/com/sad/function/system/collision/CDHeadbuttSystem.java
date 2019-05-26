package com.sad.function.system.collision;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Collidable;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Position;
import com.sad.function.system.collision.headbutt.twod.GJK;
import com.sad.function.system.collision.headbutt.twod.Headbutt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CDHeadbuttSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(CDHeadbuttSystem.class);

    private ComponentMapper<Collidable> mCollidable;
    private ComponentMapper<Position> mPosition;
    private ComponentMapper<PhysicsBody> mPhysics;

    private IntBag collidables;
    private Vector2 penetration = new Vector2();
    private Headbutt headbutt;
    private GJK gjk;

    public CDHeadbuttSystem() {
        super(Aspect.all(PhysicsBody.class));
        headbutt = new Headbutt();
        collidables = new IntBag();
        gjk = new GJK();
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void processSystem() {
        for (int a = 0; a < collidables.size(); a++) {
            for (int b = a + 1; b < collidables.size(); b++) {

                int e1 = collidables.get(a);
                int e2 = collidables.get(b);

                //TODO Will need to update the origins of the shapes assuming this works.
//                boolean val = headbutt.test(mPhysics.create(e1).hitBox, mPhysics.create(e2).hitBox);
//                boolean val = headbutt.intersects(mPhysics.create(e1).hitBox, mPhysics.create(e2).hitBox);
                boolean val = gjk.intersects(mPhysics.create(e1).hitBox, mPhysics.create(e2).hitBox);
                if (val) {
                    logger.info("COLLISION!");
                }
                //Ignore static elements.
//                if (mCollidable.create(e1).isStatic && mCollidable.create(e2).isStatic) {
//                    break;
//                }
//
//                //Are we colliding?
//                if (boxesAreColliding(e1, e2, penetration)) {
//                    //TODO Only two cases: 1. A single entity is non-static. 2. Both entities are non-static.
//                    //Both are dynamic
//                    if (!mCollidable.create(e1).isStatic && !mCollidable.create(e2).isStatic) {
//                        mCollidable.create(e1).getHandler().handleCollision(world, e2, penetration);
//                        mCollidable.create(e2).getHandler().handleCollision(world, e1, penetration);
//                    } else { //Only one is dynamic.
//                        //Figure out which one is dynamic.
//                        int dynamicEntity = mCollidable.create(e1).isStatic ? e2 : e1; //if a is static use b, otherwise just use a.
//                        int staticEntity = mCollidable.create(e1).isStatic ? e1 : e2;
//
//                        mCollidable.create(dynamicEntity).getHandler().handleCollision(world, staticEntity, penetration);
//                    }
//                }

            }
        }

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
