package com.sad.function.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.components.PositionComponent;
import com.sad.function.components.TextureComponent;
import com.sad.function.system.RenderSystem;

public class TestScreen extends BaseScreen {
    private SpriteBatch batch;

    public TestScreen(SpriteBatch batch) {
        super();

        this.batch = batch;

        initialize();
    }

    @Override
    public void initialize() {

        Entity entity = new Entity();

        entity.add(new TextureComponent());
        entity.add(new PositionComponent());

        engine().addSystem(new RenderSystem(batch));
        engine().addEntity(entity);
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }
}