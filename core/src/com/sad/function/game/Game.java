package com.sad.function.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.components.*;
import com.sad.function.global.Global;
import com.sad.function.input.InputManager;
import com.sad.function.input.InputStateManager;
import com.sad.function.screen.BaseScreen;
import com.sad.function.screen.TestScreen;
import com.sad.function.system.InputDispatchSystem;
import com.sad.function.system.RenderSystem;

public class Game extends ApplicationAdapter {
	private Engine engine;

	private SpriteBatch batch;
	private BaseScreen currentScreen;

	private InputManager inputManager = new InputManager();

	@Override
	public void create () {
		engine = new Engine();
        batch = new SpriteBatch();

		currentScreen = new TestScreen(batch);

		Gdx.input.setInputProcessor(inputManager);

		Global.activeContextsChain = Global.definedGameContexts.get(0);

		Entity playerA = new Entity();

		playerA.add(new Texture())
				.add(new Position())
				.add(new Velocity())
				.add(new PlayerInputHandler());

		//Order Matters.
		engine.addSystem(new RenderSystem(batch));
		engine.addEntity(playerA);
	}

	@Override
	public void render () {
		//Handle input.
		if(inputManager.isKeyReleased(Input.Keys.A)) {
			System.out.println("ACTION TRIGGERED");
		}

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		engine.update(Gdx.graphics.getDeltaTime());

		batch.end();

		inputManager.update();
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
