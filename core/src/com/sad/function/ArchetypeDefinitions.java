package com.sad.function;

import com.artemis.ArchetypeBuilder;
import com.sad.function.components.GravityAffected;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;

public class ArchetypeDefinitions {
    public static ArchetypeBuilder playerArchetype() {
        return new ArchetypeBuilder()
            .add(TransformComponent.class)
            .add(PhysicsBody.class)
            .add(GravityAffected.class);
    }

    public static ArchetypeBuilder solidArchetype() {
        return new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class);
    }

    public static ArchetypeBuilder bulletARchetype() {
        return new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(PhysicsBody.class);
    }
}
