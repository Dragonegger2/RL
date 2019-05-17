package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.sad.function.components.Animation;

public class AnimationSystem extends IteratingSystem {
    private ComponentMapper<Animation> mAnimation;

    public AnimationSystem() {
        super(Aspect.one(Animation.class));
    }
    @Override
    protected void process(int entityId) {
        mAnimation.create(entityId).stateTime += world.delta;
    }
}
