package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.sad.function.components.Position;
import com.sad.function.components.TextureComponent;
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

    public RenderSystem(Batch batch, OrthographicCamera camera) {
        this.entityList = new ArrayList<>();
        this.batch = batch;
        this.camera = camera;
    }

    private Vector3 calculationVector = new Vector3();
    private int renderedEntityCounter = 0;

    private void processEntity(Entity entity, float deltaTime) {
        Position position = this.position.get(entity);
        TextureComponent textureComponent = this.texture.get(entity);

        //Need to do loading of resources.
        calculationVector.x = position.x;
        calculationVector.y = position.y;

        if(camera.frustum.pointInFrustum(calculationVector)) {
            batch.draw(textureComponent.texture,
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
    }

    @Override
    public void entityRemoved(Entity entity) {
        entityList.remove(entity);
    }

    private static class ZComparator implements Comparator<Entity> {
        private ComponentMapper<Position> pm = ComponentMapper.getFor(Position.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int) Math.signum(pm.get(e1).z - pm.get(e2).z);
        }
    }
}
