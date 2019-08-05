package com.sad.function.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.sad.function.components.Lifetime;

@All(Lifetime.class)
public class LifetimeSystem extends IteratingSystem {
    protected ComponentMapper<Lifetime> mLifetime;

    @Override
    protected void process(int entityId) {
        //Tick lifetime down.
        Lifetime lifetime = mLifetime.create(entityId);
        lifetime.lifetime -= world.delta;

        //Remove it once it's life is over.
        if(lifetime.lifetime <= 0) {
            world.delete(entityId);
        }
    }
}
