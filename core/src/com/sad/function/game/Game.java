package com.sad.function.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.command.MoveLeft;
import com.sad.function.components.*;
import com.sad.function.global.Global;
import com.sad.function.input.InputActionType;
import com.sad.function.input.KeyboardManager;
import com.sad.function.screen.BaseScreen;
import com.sad.function.screen.TestScreen;
import com.sad.function.system.InputHandlingSystem;
import com.sad.function.system.PhysicsSystem;
import com.sad.function.system.RenderSystem;

public class Game extends ApplicationAdapter {
	private Engine engine;

	private SpriteBatch batch;
	private BaseScreen currentScreen;

	private KeyboardManager keyboardManager = new KeyboardManager();
	private InputHandlingSystem inputHandlingSystem = new InputHandlingSystem();

	@Override
	public void create () {
		engine = new Engine();
        batch = new SpriteBatch();

		currentScreen = new TestScreen(batch);

		Gdx.input.setInputProcessor(keyboardManager);

		Entity playerA = new Entity();

		//Register handlers
		InputHandler playerInputHandler = new InputHandler();

		//Update handlers with a name, action type, and GameCommand.
		playerInputHandler.associateAction("MOVE_LEFT", InputActionType.REPEAT_WHILE_DOWN, new MoveLeft());

		//Bind keys/buttons/etc with an action name. RAW->ACTION_NAME
		inputHandlingSystem.values.put(Input.Keys.toString(Input.Keys.LEFT), "MOVE_LEFT");

		//Register the input device with the inputHandlingSystem
		keyboardManager.addObserver(inputHandlingSystem);
		playerA.add(new Texture())
				.add(new Position())
				.add(new Velocity())
                .add(playerInputHandler);

		//Order Matters.
        engine.addSystem(inputHandlingSystem);
		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new RenderSystem(batch));
		engine.addEntity(playerA);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		keyboardManager.dispatch(delta);

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		engine.update(delta);

		batch.end();

		//Clear out the input devices.
		keyboardManager.update();
		inputHandlingSystem.clearEventQueue();
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
