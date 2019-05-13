package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sad.function.components.CameraComponent;
import com.sad.function.components.Position;

public class FollowerPlayerCamera extends IteratingSystem {
    private final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);

    private OrthographicCamera camera;

    public FollowerPlayerCamera(OrthographicCamera camera) {
        super(Family.all(CameraComponent.class, Position.class).get());

        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position pos = position.get(entity);

        camera.position.set(pos.x, pos.y, pos.z);
        camera.update();
    }
}
