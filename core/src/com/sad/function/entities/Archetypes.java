package com.sad.function.entities;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.Lifetime;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;

/**
 * Contains all Archetypes.
 */
@SuppressWarnings("ALL")
public class Archetypes {
    public Archetype aPlayer;

    public Archetype aSolid;

    public Archetype aBullet;

    public Archetype aLimitedLifetimeSolid;

    private static Archetypes instance;

    private Archetypes(final World world) {
        aPlayer = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .add(GravityAffected .class)
                .build(world);

        aSolid = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .build(world);

        aBullet = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .build(world);

        aLimitedLifetimeSolid = new ArchetypeBuilder(aSolid)
                .add(Lifetime .class)
                .build(world);
    }

    public static void createArchetypes(final World world) {
        instance = new Archetypes(world);
    }


    public static Archetypes getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Archetypes are not instantiated.");
        }

        return instance;
    }
}
