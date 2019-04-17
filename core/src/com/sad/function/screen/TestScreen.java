package com.sad.function.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.components.Position;
import com.sad.function.components.Texture;
import com.sad.function.global.Global;
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
        Global.activeContextsChain = Global.definedGameContexts.get(0);

        Entity entity = new Entity();

        entity.add(new Texture())
            .add(new Position());

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