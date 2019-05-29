package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Translation;
import com.sad.function.components.Velocity;
import com.sad.function.global.GameInfo;
import com.sad.function.system.collision.shapes.Line;
import com.sad.function.system.collision.shapes.Shape;
import com.sad.function.system.collision.utils.CollisionDetectionAlgorithms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CollisionDetectionSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(CollisionDetectionSystem.class);

    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Translation> mTranslation;
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

                if(mVelocity.has(e1)) {
                    //Try casting a ray.
                    boolean doMove = true;

                    Velocity v = mVelocity.create(e1);

                    Vector2 start = new Vector2(mTranslation.create(e1).x, mTranslation.create(e1).y);
                    Vector2 end = start.cpy().add(new Vector2(v.x, v.y)).scl(world.delta);

                    Line l = new Line(start, end);
                    Vector2 rayPenetration = gjk.intersect(l, mPhysics.create(e2).hitBox);
                    if (rayPenetration != null) {
                        logger.info("Something {}", rayPenetration);
                        mTranslation.get(e1).x -= rayPenetration.x;
                        mTranslation.get(e1).y -= rayPenetration.y;
//                        Vector2 translationDistance = new Vector2(v.x, v.y).sub(rayPenetration);
//
//                        mTranslation.create(e1).x -= translationDistance.x;
//                        mTranslation.create(e1).y -= translationDistance.y;
//                       PhysicsBody doMove = false;
                    }

//                    if (doMove) {
//                        Translation translation = mTranslation.create(e1);
//                        Velocity velocity = mVelocity.create(e1);
//
//                        velocity.x = MathUtils.clamp(velocity.x, -GameInfo.MAX_MOVEMENT_SPEED, GameInfo.MAX_MOVEMENT_SPEED);
//                        velocity.y = MathUtils.clamp(velocity.y, -GameInfo.MAX_MOVEMENT_SPEED, GameInfo.MAX_MOVEMENT_SPEED);
//
//                        translation.x += velocity.x * world.delta;
//                        translation.y += velocity.y * world.delta;
//                    }

//                Vector2 penetration = gjk.intersect(mPhysics.create(e1).hitBox, mPhysics.create(e2).hitBox);
//                if (penetration != null) {
//                    //TODO Create contact manifold.
//                    logger.info("COLLISION! {}", penetration);
//                    mTranslation.create(e1).x -= penetration.x;
//                    mTranslation.create(e1).y -= penetration.y;
//                }
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

    public class Contact {
        public Vector2 position;
        public Vector2 normal;
        public float distance;
    }

    public class Manifold {
        Shape a;
        Shape b;
        int contactCount;
        List<Contact> contacts;
    }
}
