package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sad.function.components.FollowMeComponent;
import com.sad.function.components.Position;

public class FollowerPlayerCamera extends IteratingSystem {
    private final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);

    private OrthographicCamera camera;
    private Batch batch;

    public FollowerPlayerCamera(OrthographicCamera camera, Batch batch) {
        super(Family.all(FollowMeComponent.class, Position.class).get());

        this.camera = camera;
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position pos = position.get(entity);

        float positionX = pos.x;

        camera.position.set(positionX, pos.y, pos.z);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
    }
}
