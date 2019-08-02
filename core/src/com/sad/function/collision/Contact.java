package com.sad.function.collision;

import com.sad.function.collision.data.Penetration;

import java.util.UUID;

import static java.util.Objects.hash;

public class Contact {

    private final UUID id;
    private final Body body1, body2;
    private final Fixture fixture1, fixture2;
    private final Penetration penetration;
    private final int entity1ID, entity2ID;

    private final boolean isSensor;

    public Contact(Body body1, Fixture fixture1, Body body2, Fixture fixture2, Penetration penetration, int entity1ID, int entity2ID) {
        this.id = UUID.randomUUID();
        this.body1 = body1;
        this.body2 = body2;
        this.fixture1 = fixture1;
        this.fixture2 = fixture2;
        this.penetration = penetration;

        this.entity1ID = entity1ID;
        this.entity2ID = entity2ID;
        isSensor = fixture1.isSensor() || fixture2.isSensor();
    }

    public Body getBody1() { return body1; }
    public Body getBody2() { return body2; }

    public Fixture getFixture1() { return fixture1; }
    public Fixture getFixture2() { return fixture2; }

    public UUID getId() { return id; }

    public boolean isSensor() { return isSensor; }

    @Override
    public int hashCode() {
        return hash(17,
                body1.hashCode(),
                fixture1.hashCode(),
                body2.hashCode(),
                fixture2.hashCode());
    }

    public Penetration getPenetration() {
        return penetration;
    }

    public int getEntity1ID() {
        return entity1ID;
    }

    public int getEntity2ID() { return entity2ID; }
}
