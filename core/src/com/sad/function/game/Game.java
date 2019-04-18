package com.sad.function.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.components.Position;
import com.sad.function.components.Texture;
import com.sad.function.global.Global;
import com.sad.function.input.InputStateManager;
import com.sad.function.screen.BaseScreen;
import com.sad.function.screen.TestScreen;
import com.sad.function.system.InputDispatchSystem;
import com.sad.function.system.RenderSystem;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private BaseScreen currentScreen;
	private InputStateManager inputStateManager = new InputStateManager();
	private InputDispatchSystem inputDispatchSystem = new InputDispatchSystem();

	private Engine engine;
	@Override
	public void create () {
		engine = new Engine();
        batch = new SpriteBatch();

		currentScreen = new TestScreen(batch);

		inputStateManager.addObserver(inputDispatchSystem);

		Global.activeContextsChain = Global.definedGameContexts.get(0);

		Entity entity = new Entity();

		entity.add(new Texture())
				.add(new Position());

		//Order Matters.
		engine.addSystem(inputDispatchSystem);
		engine.addSystem(new RenderSystem(batch));
		engine.addEntity(entity);
	}

	@Override
	public void render () {
		//Captures input, matches it to active contexts, and then dispatches it to whomever cares.
		inputStateManager.handleInput();

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		engine.update(Gdx.graphics.getDeltaTime());

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
