package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.global.Global;
import com.sad.function.input.BindingsLinker;
import com.sad.function.input.definitions.InputConstants;
import com.sad.function.input.definitions.InputContext;
import com.sad.function.screen.BaseScreen;
import com.sad.function.screen.TestScreen;

import java.util.HashMap;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private BaseScreen currentScreen;
	HashMap<InputConstants.Contexts, InputContext> contextToActions;

	@Override
	public void create () {
		contextToActions = BindingsLinker.readBindings();

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
		Global.textures.dispose();
	}

	public void pushScreen(BaseScreen newScreen) {
		currentScreen.exit();
		this.currentScreen = newScreen;
		currentScreen.enter();
	}
}
