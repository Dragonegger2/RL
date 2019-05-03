package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sad.function.components.CameraComponent;

public class CameraSystem extends IteratingSystem {
    private final ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);

    public CameraSystem() {
        super(Family.one(CameraComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
