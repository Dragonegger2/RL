package com.sad.function.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.event.device.DeviceConnected;
import com.sad.function.event.EventType;
import com.sad.function.global.Global;
import com.sad.function.input.devices.ControllerDevice;
import com.sad.function.input.devices.KeyboardDevice;
import com.sad.function.screen.TestScreen;
import com.sad.function.screen.WorldScreen;
import com.sad.function.system.InputHandlingSystem;

public class Game extends BaseGame {
	private SpriteBatch batch;

	private InputHandlingSystem inputHandlingSystem = new InputHandlingSystem();

	@Override
	public void create () {
		engine = new Engine();
        batch = new SpriteBatch();

        //Register device manager with the global queue.
		Global.eventQueue.addListenerByEvent(EventType.NEW_DEVICE_CONNECTED, Global.deviceManager);
		Global.eventQueue.addListenerByEvent(EventType.DEVICE_DISCONNECTED, Global.deviceManager);

		//TODO: Add polling here for controllers.
		for(Controller controller : Controllers.getControllers()) {
			controller.addListener(new ControllerDevice());
		}

        //Register keyboard with the device manager.
		Global.eventQueue.onNotify(new DeviceConnected().setDevice(new KeyboardDevice()));

		pushScreen(new WorldScreen(engine, inputHandlingSystem, batch));
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		engine.update(delta);

		batch.end();

		Global.deviceManager.clearDeviceQueues();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Global.textures.dispose();
	}
}
