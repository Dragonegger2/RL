package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.IntSet;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.Translation;
import com.sad.function.components.Velocity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dyn4j.collision.narrowphase.Gjk;
import org.dyn4j.collision.narrowphase.Raycast;
import org.dyn4j.collision.narrowphase.Separation;
import org.dyn4j.geometry.*;

public class CDSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(CDSystem.class);

    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Translation> mTranslation;
    private ComponentMapper<PhysicsBody> mPhysics;

    private IntBag collidables;

    private IntSet statics;
    private IntSet dynamics;

    private Gjk gjk;

    public CDSystem() {
        super(Aspect.all(PhysicsBody.class));
        collidables = new IntBag();

        statics = new IntSet();
        dynamics = new IntSet();

        gjk = new Gjk();
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void processSystem() {
        for (int a = 0; a < collidables.size(); a++) {
            for (int b = a + 1; b < collidables.size(); b++) {
                int e1 = collidables.get(a);
                int e2 = collidables.get(b);

                Transform t1 = new Transform();
                t1.setTranslation(mTranslation.create(e1).x, mTranslation.create(e1).y);

                Transform t2 = new Transform();
                t2.setTranslation(mTranslation.create(e2).x, mTranslation.create(e2).y);

                Convex s1 = mPhysics.create(e1).hitbox;
                Convex s2 = mPhysics.create(e2).hitbox;

                Ray ray = new Ray(new Vector2(t1.getTranslationX(), t1.getTranslationY()), new Vector2(100, 0));
                Raycast raycast = new Raycast();

                gjk.raycast(ray, 10, s2, t2, raycast);
                Separation separation = new Separation();
//                    gjk.distance(s1, t1, s2, t2, separation);
                logger.info("Data: {}", separation);

                //Fetch the central point of the dynamic entity.
                Vector2 v = new Vector2(t1.getTranslationX(), t1.getTranslationY());
                //TODO Remember, if velocity is 0,0 don't do this. It'll bitch.
                Convex s3 = new Segment(v, v.copy().add(mVelocity.create(e1).x + 10, mVelocity.create(e1).y));

                gjk.distance(s3, t1, s2, t2, separation);
                logger.info("Data: {}", separation);

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
