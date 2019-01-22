package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sad.function.components.PositionComponent;
import com.sad.function.components.TextureComponent;
import com.sad.function.global.Global;
import com.sad.function.global.Mappers;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {

    private Batch batch;

    public RenderSystem(Batch batch) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), new ZComparator());

        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        TextureComponent texture = Mappers.texture.get(entity);

        //Need to do loading of resources.
        if(batch.isDrawing()) {
            batch.draw(Global.textures.get(texture.internalPath), position.x, position.y);
        }
    }

    private static class ZComparator implements Comparator<Entity> {
        private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)Math.signum(pm.get(e1).z - pm.get(e2).z);
        }
    }
}
