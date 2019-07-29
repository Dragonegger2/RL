package com.sad.function.systems;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.*;
import com.sad.function.collision.data.Penetration;
import com.sad.function.collision.detection.narrowphase.GJK;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;

import java.util.ArrayList;
import java.util.List;

@All({PhysicsBody.class, TransformComponent.class})
public class PhysicsSystem extends BaseEntitySystem {
    private static final int PHYSIC_SUB_STEPS = 10; //TODO Make configurable.

    private final Vector2 gravity = new Vector2(0, -100.0f);

    private final ContactManager contactManager;
    protected ComponentMapper<GravityAffected> mGravityAffected;
    private ComponentMapper<PhysicsBody> mPhysics;
    private ComponentMapper<TransformComponent> mTransform;
    private GJK gjk;
    private List<Listener> listeners = new ArrayList<>();

    public PhysicsSystem() {
        gjk = new GJK();
        contactManager = new ContactManager();
    }

    @Override
    protected void processSystem() {
        applyGravity();

        //apply velocity.
        IntBag actives = subscription.getEntities();
        float splitStep = world.delta / PHYSIC_SUB_STEPS;
        for (int i = 0; i < PHYSIC_SUB_STEPS; i++) {
            //Apply velocity.
            for (int j = 0; j < actives.size(); j++) {
                int entity = actives.get(j);
                Body body = mPhysics.create(entity).body;

                if (body.isStatic()) continue;

                mTransform.create(entity).transform.translate(body.getVelocity().cpy().scl(splitStep));
            }

            //handle collisions.
            handleBodies();
        }

        contactManager.updateAndNotify(getListeners(ContactListener.class));
    }

    private void applyGravity() {
        IntBag actives = subscription.getEntities();
        for (int i = 0; i < actives.size(); i++) {
            int entity = actives.get(i);
            //TODO For some reason the entity bag has dozens of 0's.
            Body body = mPhysics.create(entity).body;

            if (mGravityAffected.has(entity)) {
                GravityAffected gravityAffected = mGravityAffected.create(entity);
                body.getVelocity().add(
                        gravity.x * gravityAffected.gravityScale * world.getDelta(),
                        gravity.y * gravityAffected.gravityScale * world.getDelta());
            }
        }
    }

    private void handleBodies() {
        IntBag actives = subscription.getEntities();
        for (int i = 0; i < actives.size(); i++) {
            int entity = actives.get(i);
            Body body = mPhysics.create(entity).body;

            if (body.isStatic()) continue;

            checkFixtureCollisions(body, entity);
        }
    }

    private void checkFixtureCollisions(Body body1, int body1ID) {
        IntBag actives = subscription.getEntities();

        for (int i = 0; i < actives.size(); i++) {
            int entity = actives.get(i);
            Body body2 = mPhysics.create(entity).body;

            if (body1 == body2) continue;

            int fc1 = body1.getFixtureCount();
            int fc2 = body2.getFixtureCount();

            for (int j = 0; j < fc1; j++) {
                Fixture fixture1 = body1.getFixtures().get(j);
                for (int k = 0; k < fc2; k++) {
                    Fixture fixture2 = body2.getFixtures().get(k);

                    Penetration penetration = new Penetration();
                    if (gjk.detect(fixture1.getShape(), mTransform.create(body1ID).transform,
                            fixture2.getShape(), mTransform.create(entity).transform, penetration)) {

                        //TODO: Going to need to modify this so that sensors on any body will work.
                        if (!fixture1.isSensor() && !fixture2.isSensor()) { //Neither are sensors...
                            //BODY1 is always the dynamic shape.
                            //All of this logic could be moved to a handler.
                            Vector2 translation = penetration.normal.cpy().scl(-1).scl(penetration.distance);

                            mTransform.create(body1ID).transform.translate(translation);

                            if (penetration.normal.x != 0) {
                                body1.getVelocity().x = 0;
                            }

                            if (penetration.normal.y != 0) {
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
     *
     * @param listener to register.
     */
    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    /**
     * Fetch all registered listeners to this world object that match the class type.
     *
     * @param clazz listener type to fetch.
     * @param <T>   Type parameter
     * @return list of listeners OR null if clazz is null.
     */
    public <T extends Listener> List<T> getListeners(Class<T> clazz) {
        if (clazz == null) return null;
        List<T> listeners = new ArrayList<T>();

        int lSize = this.listeners.size();
        for (int i = 0; i < lSize; i++) {
            Listener listener = this.listeners.get(i);
            if (clazz.isInstance(listener)) {
                listeners.add(clazz.cast(listener));
            }
        }

        return listeners;
    }

    /**
     * Sets the current gravity of this {@link com.artemis.World}
     * <p>
     * Default is (0, -9.8f).
     *
     * @param x value of gravity.
     * @param y value of gravity.
     */
    public void setGravity(float x, float y) {
        gravity.set(x, y);
    }

    public Vector2 getGravity() {
        return gravity;
    }

    /**
     * Sets the current gravity of this {@link com.artemis.World}
     * <p>
     * Default is (0, -9.8f).
     *
     * @param gravity new value of gravity.
     */
    public void setGravity(Vector2 gravity) {
        this.gravity.set(gravity);
    }
}