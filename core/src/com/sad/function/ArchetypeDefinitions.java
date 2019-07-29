package com.sad.function;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;

public class ArchetypeDefinitions {
    public static Archetype aPlayer;
    public static Archetype aSolid;
    public static Archetype aBullet;

    public ArchetypeDefinitions(World gameWorld) {
        aPlayer = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .add(GravityAffected.class)
                .build(gameWorld);

        aSolid = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .build(gameWorld);

        aBullet = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class)
                .build(gameWorld);
    }
}
