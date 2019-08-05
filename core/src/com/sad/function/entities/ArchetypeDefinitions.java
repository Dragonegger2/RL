package com.sad.function.entities;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.Lifetime;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;

/**
 * Contains all ArchetypeDefinitions.
 */
public class ArchetypeDefinitions {
    private Archetype aPlayer;

    private Archetype aSolid;

    private Archetype aBullet;

    private Archetype aLimitedLifetimeSolid;

    public ArchetypeDefinitions(World world) {

    }

    public Archetype aPlayer() { return aPlayer; }
    public Archetype aSolid() { return aSolid; }
    public Archetype aBullet() { return aBullet; }
    public Archetype aLimitedLifetimeSolid() { return aLimitedLifetimeSolid; }
}
