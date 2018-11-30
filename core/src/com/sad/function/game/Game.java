package com.sad.function.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.components.PositionComponent;
import com.sad.function.components.TextureComponent;
import com.sad.function.global.Global;
import com.sad.function.system.RenderSystem;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch = new SpriteBatch();

	@Override
	public void create () {
        Entity entity = new Entity();
        entity.add(new TextureComponent());
        entity.add(new PositionComponent());

        Global.engine().addSystem(new RenderSystem(batch));
        Global.engine().addEntity(entity);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

            Global.engine().update(Gdx.graphics.getDeltaTime());

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Global.textures().dispose();
	}
}
