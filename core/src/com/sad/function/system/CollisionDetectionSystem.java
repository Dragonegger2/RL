package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import com.sad.function.components.Collidable;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Position;
import com.sad.function.system.collision.utils.CollisionDetectionAlgorithms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CollisionDetectionSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(CollisionDetectionSystem.class);

    private ComponentMapper<Collidable> mCollidable;
    private ComponentMapper<Position> mPosition;
    private ComponentMapper<PhysicsBody> mPhysics;

    private IntBag collidables;

    private IntSet statics;
    private IntSet dynamics;

    private CollisionDetectionAlgorithms gjk;

    public CollisionDetectionSystem() {
        super(Aspect.all(PhysicsBody.class));
        collidables = new IntBag();

        statics = new IntSet();
        dynamics = new IntSet();

        gjk = new CollisionDetectionAlgorithms();
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void processSystem() {
        for (int a = 0; a < collidables.size(); a++) {
            for (int b = a + 1; b < collidables.size(); b++) {

                int e1 = collidables.get(a);
                int e2 = collidables.get(b);

                Vector2 penetration = gjk.intersect(mPhysics.create(e1).hitBox, mPhysics.create(e2).hitBox);
                if (penetration != null) {
                    logger.info("COLLISION! {}", penetration);
                    mPhysics.create(e1).hitBox.getOrigin().sub(penetration);
                }

            }
        }
    }

    @Override
    public void inserted(int entity) {
        collidables.add(entity);

        if (mPhysics.create(entity).dynamic) {
            dynamics.add(entity);
        } else {
            statics.add(entity);
        }
    }

    @Override
    public void removed(int entity) {
        if (collidables.indexOf(entity) != -1) {
            collidables.remove(collidables.indexOf(entity));
        }
    }
}
