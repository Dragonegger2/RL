package com.sad.function.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sad.function.command.*;
import com.sad.function.components.InputHandler;
import com.sad.function.components.Position;
import com.sad.function.components.TextureComponent;
import com.sad.function.components.Velocity;
import com.sad.function.global.Global;
import com.sad.function.input.states.InputActionType;
import com.sad.function.input.context.InputContext;
import com.sad.function.system.InputHandlingSystem;
import com.sad.function.system.RenderSystem;

@SuppressWarnings("ALL")
public class TestScreen extends BaseScreen {

    public TestScreen(Engine engine, InputHandlingSystem inputHandlingSystem, SpriteBatch batch) {
        super();

        initialize(engine, inputHandlingSystem, batch);
    }

    private void initialize(Engine engine, InputHandlingSystem inputHandlingSystem, SpriteBatch batch) {
        InputHandler playerInputHandler = new InputHandler();

        //LOAD IN GAME STATE STUFFS
        Entity playerA = new Entity().add(new TextureComponent())
                .add(new Position())
                .add(new Velocity())
                .add(playerInputHandler);


        float velocity = 100f;
        //Update handlers with a type, action type, and GameCommand.
//        playerInputHandler.associateAction("MOVE_LEFT", InputActionType.REPEAT_WHILE_DOWN, new MoveLeft(velocity));
//        playerInputHandler.associateAction("MOVE_RIGHT", InputActionType.REPEAT_WHILE_DOWN, new MoveRight(velocity));
//        playerInputHandler.associateAction("MOVE_UP", InputActionType.REPEAT_WHILE_DOWN, new MoveUp(velocity));
//        playerInputHandler.associateAction("MOVE_DOWN", InputActionType.REPEAT_WHILE_DOWN, new MoveDown(velocity));
        playerInputHandler.associateAction("QUIT", InputActionType.ON_PRESS_ONLY, new QuitGame());

        InputContext testContext = new InputContext("TEST_CONTEXT");

        testContext.addMappedInput("Left", "MOVE_LEFT");
        testContext.addMappedInput("Right", "MOVE_RIGHT");
        testContext.addMappedInput("Up", "MOVE_UP");
        testContext.addMappedInput("Down", "MOVE_DOWN");
        testContext.addMappedInput("Escape", "QUIT");

        inputHandlingSystem.pushContext(testContext);

        Global.deviceManager.assignDevice(playerInputHandler);

        //Register systems
        engine.addSystem(inputHandlingSystem);
        engine.addSystem(new RenderSystem(batch));

        engine.addEntity(playerA);
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }
}