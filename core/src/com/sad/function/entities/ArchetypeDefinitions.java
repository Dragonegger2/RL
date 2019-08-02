package com.sad.function.entities;

import com.artemis.ArchetypeBuilder;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;

/**
 * Contains all ArchetypeDefinitions.
 */
public class ArchetypeDefinitions {
    public static ArchetypeBuilder aPlayer = new ArchetypeBuilder()
            .add(TransformComponent.class)
            .add(PhysicsBody.class)
            .add(GravityAffected.class);

    public static ArchetypeBuilder aSolid = new ArchetypeBuilder()
            .add(TransformComponent.class)
            .add(PhysicsBody.class);

    public static ArchetypeBuilder aBullet = new ArchetypeBuilder()
            .add(TransformComponent.class)
            .add(PhysicsBody.class);
}
