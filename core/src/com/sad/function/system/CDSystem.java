package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.IntSet.IntSetIterator;
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
        IntSetIterator d = dynamics.iterator();

        while(d.hasNext) {
            int dId = d.next();

            IntSetIterator s = statics.iterator();
            while(s.hasNext) {
                int sId = s.next();


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
