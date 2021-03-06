package com.sad.function.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.screen.BaseScreen;

public class BaseGame extends ApplicationAdapter {
	protected Engine engine;
	protected SpriteBatch batch;
	protected BaseScreen currentScreen;

	/**
	 * Transitions between two screens. Calls the exit method of the screen that is going to be replaced, allowing
	 * us to store data, display graphics, etc.
	 *
	 * Then load up the new screen and invoke it's enter function, for much the same reason as above.
	 *
	 * @param newScreen the new screen to migrate the game to.
	 */
	public void pushScreen(BaseScreen newScreen) {
		if(currentScreen != null) {
			currentScreen.exit();
		}
		this.currentScreen = newScreen;
		currentScreen.enter();
	}
}
