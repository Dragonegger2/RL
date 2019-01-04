package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.global.Global;
import com.sad.function.screen.BaseScreen;
import com.sad.function.screen.TestScreen;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private BaseScreen currentScreen;

	@Override
	public void create () {
        batch = new SpriteBatch();
		currentScreen = new TestScreen(batch);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

            currentScreen.engine().update(Gdx.graphics.getDeltaTime());

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Global.textures().dispose();
	}

	public void pushScreen(BaseScreen newScreen) {
		currentScreen.exit();
		this.currentScreen = newScreen;
		currentScreen.enter();
	}
}
