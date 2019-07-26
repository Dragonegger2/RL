package com.sad.function.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.*;
import com.sad.function.collision.data.Penetration;
import com.sad.function.collision.detection.narrowphase.GJK;
import com.sad.function.components.PhysicsComponent;
import com.sad.function.components.TransformComponent;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem extends BaseEntitySystem {
    //TODO MASSIVE TODO use the Transform Component instead of the transform in the bodies.
    //I wonder if I should just refactor out the transform component from a body...

    private ComponentMapper<PhysicsComponent> mPhysics;
    private Vector2 gravity = new Vector2(0, -9.8f);
    private static final int PHYSIC_SUB_STEPS = 10;
    private final ContactManager contactManager;

    private GJK gjk;
    private IntBag registeredBodies;
    private List<Listener> listeners = new ArrayList<>();

    public PhysicsSystem() {
        super(Aspect.all(PhysicsComponent.class, TransformComponent.class));

        gjk = new GJK();
        contactManager = new ContactManager();
        registeredBodies = new IntBag();
    }

    @Override
    public void inserted(int entity) {
        registeredBodies.add(entity);
    }

    @Override
    public void removed(int entity) {
        registeredBodies.remove(entity);
    }

    @Override
    protected void processSystem() {
        //apply gravity.
        int[] ids = registeredBodies.getData();
        for(int entity : ids) {
            Body body = mPhysics.create(entity).body;
            if(body.isStatic()) continue;

            body.getVelocity().add(gravity.cpy().scl(world.delta).scl(body.getGravityScale()));
        }

        //apply velocity.
        float splitStep = world.delta / PHYSIC_SUB_STEPS;
        for(int i = 0; i < PHYSIC_SUB_STEPS; i++) {
            //Apply velocity.
            for(int entity : ids) {
                Body body = mPhysics.create(entity).body;

                if(body.isStatic()) continue;

                body.translate(body.getVelocity().cpy().scl(splitStep));
            }

            //handle collisions.
            handleBodies();
        }

        contactManager.updateAndNotify(getListeners(ContactListener.class));
    }

    private void handleBodies() {
        int[] ids = registeredBodies.getData();
        for(int entity : ids) {
            Body body = mPhysics.create(entity).body;

            if(body.isStatic()) continue;

            checkFixtureCollisions(body);
        }
    }

    private void checkFixtureCollisions(Body body1) {
        int[] ids = registeredBodies.getData();

        for(int entity : ids) {
            Body body2 = mPhysics.create(entity).body;

            if(body1 == body2) continue;

            int fc1 = body1.getFixtureCount();
            int fc2 = body2.getFixtureCount();

            for(int j = 0; j < fc1; j++) {
                Fixture fixture1 = body1.getFixtures().get(j);
                for(int k = 0; k < fc2; k++) {
                    Fixture fixture2 = body2.getFixtures().get(k);

                    Penetration penetration = new Penetration();
                    if(gjk.detect(fixture1.getShape(), body1.getTransform(), fixture2.getShape(), body2.getTransform(), penetration)) {

                        //ToDO: Goign to need to modify this so that sensors on any body will work.
                        if(!fixture1.isSensor() && !fixture2.isSensor()) { //Neither are sensors...
                            //BODY1 is always the dynamic shape.
                            //All of this logic could be moved to a handler.
                            Vector2 translation = penetration.normal.cpy().scl(-1).scl(penetration.distance);
                            body1.translate(translation);

                            if(penetration.normal.x != 0) {
                                body1.getVelocity().x = 0;
                            }

                            if(penetration.normal.y != 0) {
                                body1.getVelocity().y = 0;
                            }
                        }

                        //Still need to notify if they are Sensors.
                        contactManager.addContact(new Contact(body1, fixture1, body2, fixture2, penetration));
                    }
                }
            }
        }
    }

    /**
     * Register a listener.
     * @param listener to register.
     */
    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    /**
     * Fetch all registered listeners to this world object that match the class type.
     * @param clazz listener type to fetch.
     * @param <T> Type parameter
     * @return list of listeners OR null if clazz is null.
     */
    public <T extends Listener> List<T> getListeners(Class<T> clazz) {
        if(clazz == null) return null;
        List<T> listeners = new ArrayList<T>();

        int lSize = this.listeners.size();
        for(int i = 0; i < lSize; i++) {
            Listener listener = this.listeners.get(i);
            if(clazz.isInstance(listener)) {
                listeners.add(clazz.cast(listener));
            }
        }

        return listeners;
    }
}