package com.sad.function.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sad.function.World.Tile;
import com.sad.function.World.World;
import com.sad.function.World.WorldGenerator;
import com.sad.function.command.QuitGame;
import com.sad.function.command.movement.MoveHorizontally;
import com.sad.function.command.movement.MoveVertically;
import com.sad.function.components.*;
import com.sad.function.global.Global;
import com.sad.function.input.context.InputContext;
import com.sad.function.input.states.InputActionType;
import com.sad.function.system.FollowerPlayerCamera;
import com.sad.function.system.InputHandlingSystem;
import com.sad.function.system.MovementSystem;
import com.sad.function.system.PhysicsSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("ALL")
public class WorldScreen extends BaseScreen {
    private Logger logger = LogManager.getLogger(WorldScreen.class);

    private World world;
    private Engine engine;
    private OrthographicCamera camera;
    private int WORLD_HEIGHT = 500*32;
    private int WORLD_WIDHT = 500*32;

    private WorldGenerator worldGenerator = new WorldGenerator(10, 500, 500);

    public WorldScreen(Engine engine, InputHandlingSystem inputHandlingSystem, OrthographicCamera camera) {
        this.engine = engine;
        this.camera = camera;
        initialize(inputHandlingSystem);


        worldGenerator.initializeWorld();
        registerTilemapInECS();
    }

    private void initialize(InputHandlingSystem inputHandlingSystem) {
        InputHandler playerInputHandler = new InputHandler();

        //LOAD IN GAME STATE STUFFS
        Entity playerA = new Entity()
                .add(new TextureComponent().setHeight(32).setWidth(32))
                .add(new VelocityComponent())
                .add(new Position().setZ(1))
                .add(new Physics())
                .add(new CameraComponent())
                .add(new AnimationComponent())
                .add(playerInputHandler);

        float velocity = 300f;

        //Update handlers with a type, action type, and GameCommand.
        playerInputHandler.associateAction("MOVE_LEFT", InputActionType.REPEAT_WHILE_DOWN, new MoveHorizontally(-velocity));
        playerInputHandler.associateAction("MOVE_RIGHT", InputActionType.REPEAT_WHILE_DOWN, new MoveHorizontally(velocity));

        playerInputHandler.associateAction("MOVE_UP", InputActionType.REPEAT_WHILE_DOWN, new MoveVertically(velocity));
        playerInputHandler.associateAction("MOVE_DOWN", InputActionType.REPEAT_WHILE_DOWN, new MoveVertically(-velocity));
        playerInputHandler.associateAction("QUIT", InputActionType.ON_PRESS_ONLY, new QuitGame());

        InputContext testContext = new InputContext("TEST_CONTEXT");

        testContext.addMappedInput("Left", "MOVE_LEFT");
        testContext.addMappedInput("Right", "MOVE_RIGHT");
        testContext.addMappedInput("Up", "MOVE_UP");
        testContext.addMappedInput("Down", "MOVE_DOWN");
        testContext.addMappedInput("Escape", "QUIT");

        inputHandlingSystem.pushContext(testContext);

        Global.deviceManager.assignDevice(playerInputHandler);

        //Register systems to be managed by the ECS
        engine.addSystem(new MovementSystem());
        engine.addSystem(new PhysicsSystem());
        engine.addSystem(new FollowerPlayerCamera(camera));

        engine.addEntity(playerA);
    }

    private void registerTilemapInECS() {
        logger.info("Registering tiles in ECS.");
        Tile[][] map = worldGenerator.getWorld();
        for(int x = 0; x < worldGenerator.getWorldHeight(); x++ ){
            for(int y = 0; y < worldGenerator.getWorldWidth(); y++) {
                engine.addEntity(map[x][y].getTileEntity());
            }
        }

        logger.info("Finished creating {} tile entities.", worldGenerator.getWorldHeight() * worldGenerator.getWorldWidth());
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }
}