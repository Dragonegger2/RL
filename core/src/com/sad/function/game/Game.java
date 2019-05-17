package com.sad.function.game;

//TODO: Optimize game components to pull from a pool or other collection to minimize impact in memory. Even though I've got 250,000 it shouldn't be running at sub 30 frames.
//Doesn't help I generate 10,000 * 10,000 tiles and then convert those into entities. At least they reuse the same texture. I think...
/*
    They do reuse the same texture now, but they're not exactly lightweight objects. For ground tiles, I should look
    into a Flyweight pattern object for them. At least the texture component. That would actually be pretty simple.

    Animation (light weight)
        - Current time
        - Animation name

    Texture Component (holds actual texture/atlas information)
        - Internal Name for texture

    Position

    Physics/Something else
        - Has size information. Not sure it should be a part of the texture object.
            This makes some more sense.

 */
public class Game extends BaseGame {
//    private static final Logger logger = LogManager.getLogger(Game.class);
//
//    private ResourceManager resourceManager;
//
//    private InputHandlingSystem inputHandlingSystem = new InputHandlingSystem();
//    private RenderSystem renderingSystem;
//
//    private OrthographicCamera camera;
//
//    @Override
//    public void create() {
//        resourceManager = new ResourceManager();
//
//        engine = new Engine();
//        batch = new SpriteBatch();
//
//        setupCamera();
//
//        renderingSystem = new RenderSystem(resourceManager, batch, camera);
//
//        //register ecs listeners.
//        engine.addEntityListener(renderingSystem.getFamily(), renderingSystem);
//        engine.addEntityListener(Family.one(InputHandler.class).get(), inputHandlingSystem);
//
//        //Register device manager with the global queue.
//        Global.eventQueue.addListenerByEvent(EventType.NEW_DEVICE_CONNECTED, Global.deviceManager);
//        Global.eventQueue.addListenerByEvent(EventType.DEVICE_DISCONNECTED, Global.deviceManager);
//
//        //TODO: Add polling here for controllers.
////		for(Controller controller : Controllers.getControllers()) {
////			controller.addListener(new ControllerDevice());
////		}
//
//        //Register keyboard with the device manager.
//        Global.eventQueue.onNotify(new DeviceConnected().setDevice(new KeyboardDevice()));
//
//        WorldScreen world = new WorldScreen(engine, inputHandlingSystem, camera);
//        pushScreen(world);
//    }
//    private void setupCamera() {
//        float w = Gdx.graphics.getWidth();
//        float h = Gdx.graphics.getHeight();
//
//        float viewportWidth = 180;
//        camera = new OrthographicCamera(viewportWidth,viewportWidth * (h/w));
//
//        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight/ 2f, 0);
//        camera.update();
//    }
//
//    private float accumulator = 0f;
//
//    @Override
//    public void render() {
//        final float TARGET_FRAME_RATE = 1.0f/90.0f;
//        while (!Global.assetManager.update()) {
//            logger.info("Loading assets {}% complete", Global.assetManager.getProgress() * 100);
//        }
//
//        while(accumulator <= TARGET_FRAME_RATE) {
//            float delta = Gdx.graphics.getDeltaTime();
//            accumulator += delta;
//
//            inputHandlingSystem.handleInput(delta);
//
//            engine.update(delta);
//
//            Global.deviceManager.clearDeviceQueues();
//        }
//
//
//        renderingSystem.render(accumulator);
//
//        Gdx.graphics.setTitle(String.format("FPS: %s", Gdx.graphics.getFramesPerSecond()));
//        accumulator = 0;
//    }
//
//    @Override
//    public void dispose() {
//        batch.dispose();
//
//        Global.assetManager.dispose();
//
//        resourceManager.dispose();
//    }
}
