package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.sad.function.components.CameraComponent;
import com.sad.function.components.PhysicsBody;

public class CameraSystem extends IteratingSystem {
    private ComponentMapper<PhysicsBody> mPhysicsBody;

    private Camera camera;

    public CameraSystem(Camera camera) {
        super(Aspect.all(CameraComponent.class));

        this.camera = camera;
    }

    @Override
    protected void process(int entityId) {
        PhysicsBody pos = mPhysicsBody.create(entityId);

        camera.position.set(pos.body.getPosition().x,
                pos.body.getPosition().y,
                0);
    }
}
