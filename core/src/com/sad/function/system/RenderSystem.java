package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sad.function.components.Position;
import com.sad.function.components.TextureComponent;
import com.sad.function.global.Global;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RenderSystem implements EntityListener {
    private static final Logger logger = LogManager.getLogger(RenderSystem.class);
    private static final ZComparator zComparator = new ZComparator();
    private final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    private final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    private final Family renderFamily = Family.all(Position.class, TextureComponent.class).get();
    private OrthographicCamera camera;
    private Batch batch;
    private List<Entity> entityList;

    private boolean dirty = false;

    //NOTES: Pretty sure that the camera in LibGDX measures from the center, but sprites measure from their lower-left hand corner.
    private int renderedEntityCounter = 0;

    public RenderSystem(Batch batch, OrthographicCamera camera) {
        this.entityList = new ArrayList<>();
        this.batch = batch;
        this.camera = camera;
    }

    private void processEntity(Entity entity, float deltaTime) {
        Position position = this.position.get(entity);
        TextureComponent textureComponent = this.texture.get(entity);

        if (camera.frustum.pointInFrustum(position.x, position.y, position.z) ||
                camera.frustum.pointInFrustum(position.x + textureComponent.width, position.y, position.z) ||
                camera.frustum.pointInFrustum(position.x + textureComponent.width, position.y + textureComponent.height, position.z) ||
                camera.frustum.pointInFrustum(position.x, position.y + textureComponent.height, position.z)) {
            batch.draw(Global.assetManager.get(textureComponent.internalPath, Texture.class),
                    position.x,
                    position.y,
                    textureComponent.width,
                    textureComponent.height);
            renderedEntityCounter++;
        }
    }

    /**
     * Full render.
     *
     * @param deltaTime, used for lerp between rendering cycles in the future.
     */
    public void render(float deltaTime) {
        if (dirty) {
            entityList.sort(zComparator);
            dirty = false;
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (Entity entity : entityList) {
            processEntity(entity, deltaTime);
        }

        batch.end();
        logger.info("Rendered {} object(s)", renderedEntityCounter);
        renderedEntityCounter = 0;
    }

    @Override
    public void entityAdded(Entity entity) {
        if (renderFamily.matches(entity)) {
            dirty = true;
            entityList.add(entity);
        }
        //TODO: Look into having this also register entities that have a camera class.
    }

    @Override
    public void entityRemoved(Entity entity) {
        entityList.remove(entity);
    }

    public double distance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x2 - x2, 2) + Math.pow(y2 - y1, 2));
    }

    private static class ZComparator implements Comparator<Entity> {
        private ComponentMapper<Position> pm = ComponentMapper.getFor(Position.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int) Math.signum(pm.get(e1).z - pm.get(e2).z);
        }
    }
}
