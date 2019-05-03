package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sad.function.components.Position;
import com.sad.function.components.TextureComponent;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {
    private final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    private final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);

    private Batch batch;

    public RenderSystem(Batch batch) {
        super(Family.all(Position.class, TextureComponent.class).get(), new ZComparator());

        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position position = this.position.get(entity);
        TextureComponent textureComponent = this.texture.get(entity);

        //Need to do loading of resources.
        if(batch.isDrawing()) {
            batch.draw(textureComponent.getTexture(), position.x, position.y);
        }
    }

    private static class ZComparator implements Comparator<Entity> {
        private ComponentMapper<Position> pm = ComponentMapper.getFor(Position.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)Math.signum(pm.get(e1).z - pm.get(e2).z);
        }
    }
}
