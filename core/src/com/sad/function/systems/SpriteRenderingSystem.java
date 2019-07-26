package com.sad.function.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sad.function.components.TransformComponent;
import com.sad.function.global.GameInfo;

@All(TransformComponent.class)
public class SpriteRenderingSystem extends IteratingSystem {
    protected ComponentMapper<TransformComponent> mTransformComponent;

    private OrthographicCamera camera;

    public SpriteRenderingSystem(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    protected void process(int entityId) {

        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(GameInfo.RENDER_SPRITES) {
            //TODO: Pull Render sprite logic from previous versions.
        }
    }
}
