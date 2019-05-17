package com.sad.function.system;

import com.sad.function.components.CameraComponent;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.sad.function.components.Dimension;
import com.sad.function.components.Position;

public class CameraSystem extends IteratingSystem {
    private ComponentMapper<Position> mPosition;
    private ComponentMapper<Dimension> mDimension;

    private Camera camera;

    public CameraSystem(Camera camera) {
        super(Aspect.all(CameraComponent.class, Position.class, Dimension.class));

        this.camera = camera;
    }

    @Override
    protected void process(int entityId) {
        Position pos = mPosition.create(entityId);
        Dimension dimension = mDimension.create(entityId);

        camera.position.set(pos.x + dimension.width / 2, pos.y + dimension.height / 2, pos.z);
    }
}
