package com.sad.function.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.global.Global;
import com.sad.function.input.KeyboardInputProcessor;
import com.sad.function.screen.BaseScreen;
import com.sad.function.screen.TestScreen;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private BaseScreen currentScreen;
	private KeyboardInputProcessor keyboardInputProcessor = new KeyboardInputProcessor();

	@Override
	public void create () {
        batch = new SpriteBatch();
		currentScreen = new TestScreen(batch);

		Gdx.input.setInputProcessor(keyboardInputProcessor);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Push Keyboard state up.
		Global.keyboardStates.push(keyboardInputProcessor.getCurrentKeyboardStatus());
		//

		batch.begin();
		currentScreen.engine().update(Gdx.graphics.getDeltaTime());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Global.textures.dispose();
	}

	/**
	 * Transitions between two screens. Calls the exit method of the screen that is going to be replaced, allowing
	 * us to store data, display graphics, etc.
	 *
	 * Then load up the new screen and invoke it's enter function, for much the same reason as above.
	 *
	 * @param newScreen the new screen to migrate the game to.
	 */
	public void pushScreen(BaseScreen newScreen) {
		currentScreen.exit();
		this.currentScreen = newScreen;
		currentScreen.enter();
	}
}
