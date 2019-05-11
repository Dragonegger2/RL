package com.sad.function.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.components.Position;
import com.sad.function.components.TextureComponent;
import com.sad.function.event.EventType;
import com.sad.function.event.device.DeviceConnected;
import com.sad.function.global.Global;
import com.sad.function.input.devices.KeyboardDevice;
import com.sad.function.loaders.JsonLoader;
import com.sad.function.screen.WorldScreen;
import com.sad.function.system.InputHandlingSystem;
import com.sad.function.system.RenderSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Game extends BaseGame {
    private static final Logger logger = LogManager.getLogger(Game.class);
    private InputHandlingSystem inputHandlingSystem = new InputHandlingSystem();
    private RenderSystem renderingSystem;
    private OrthographicCamera camera;

    @Override
    public void create() {
        engine = new Engine();
        batch = new SpriteBatch();

        //Load assets from input/assets.json
        JsonLoader.load();

        setupCamera();

        renderingSystem = new RenderSystem(batch, camera);

        //register ecs listeners.
        engine.addEntityListener(Family.all(Position.class, TextureComponent.class).get(), renderingSystem);

        //Register device manager with the global queue.
        Global.eventQueue.addListenerByEvent(EventType.NEW_DEVICE_CONNECTED, Global.deviceManager);
        Global.eventQueue.addListenerByEvent(EventType.DEVICE_DISCONNECTED, Global.deviceManager);

        //TODO: Add polling here for controllers.
//		for(Controller controller : Controllers.getControllers()) {
//			controller.addListener(new ControllerDevice());
//		}

        //Register keyboard with the device manager.
        Global.eventQueue.onNotify(new DeviceConnected().setDevice(new KeyboardDevice()));

        WorldScreen world = new WorldScreen(engine, inputHandlingSystem, camera);
        pushScreen(world);
    }

    private void setupCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        float viewportWidth = 180;
        camera = new OrthographicCamera(viewportWidth,viewportWidth * (h/w));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight/ 2f, 0);
        camera.update();
    }

    @Override
    public void render() {
        while (!Global.assetManager.update()) {
            logger.info("Loading assets {}% complete", Global.assetManager.getProgress() * 100);
        }

        float delta = Gdx.graphics.getRawDeltaTime();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);

        renderingSystem.render(delta);

        Global.deviceManager.clearDeviceQueues();
        Gdx.graphics.setTitle(String.format("FPS: %s", 1f/delta));
    }

    @Override
    public void dispose() {
        batch.dispose();
        Global.textures.dispose();
    }
}
