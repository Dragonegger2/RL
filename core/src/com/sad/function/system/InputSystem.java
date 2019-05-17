package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.IntArray;
import com.sad.function.command.GameCommand;
import com.sad.function.command.NullGameCommand;
import com.sad.function.command.QuitGame;
import com.sad.function.command.movement.MoveHorizontally;
import com.sad.function.command.movement.MoveVertically;
import com.sad.function.common.DefaultMap;
import com.sad.function.components.Input;
import com.sad.function.global.Global;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class InputSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(InputSystem.class);

    private IntArray entitiesWithInputHandlers;
    private Map<Action, GameCommand> actions;

    public InputSystem() {
        super(Aspect.all(Input.class));

        entitiesWithInputHandlers = new IntArray();
        actions = new DefaultMap<>(new NullGameCommand());

        float velocity = 140f;
        actions.put(Action.MOVE_LEFT, new MoveHorizontally(-velocity));
        actions.put(Action.MOVE_RIGHT, new MoveHorizontally(velocity));
        actions.put(Action.MOVE_UP, new MoveVertically(velocity));
        actions.put(Action.MOVE_DOWN, new MoveVertically(-velocity));

        actions.put(Action.QUIT_GAME, new QuitGame());
    }

    @Override
    protected void processSystem() {
        for (int entity : entitiesWithInputHandlers.items) {
            processEntity(entity, Global.DELTA);
        }
    }

    private void processEntity(int entity, float deltaTime) {
        //TODO push actions into a queue or something.
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            actions.get(Action.MOVE_LEFT).execute(world, entity, deltaTime);
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            actions.get(Action.MOVE_RIGHT).execute(world, entity, deltaTime);
        }

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            actions.get(Action.MOVE_UP).execute(world, entity, deltaTime);
        }

        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            actions.get(Action.MOVE_DOWN).execute(world, entity, deltaTime);
        }

        if (!Gdx.input.isKeyPressed(Keys.DOWN) && Gdx.input.isKeyPressed(Keys.UP) && Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.RIGHT)) {
            actions.get(Action.STOP).execute(world, entity, deltaTime);
        }

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            actions.get(Action.QUIT_GAME).execute(world, entity, deltaTime);
        }
    }

    @Override
    protected void inserted(int entity) {
        logger.info("Added new entity with input handler {}.", entity);
        entitiesWithInputHandlers.add(entity);
    }

    @Override
    protected void removed(int entity) {
        logger.info("Removed entity with input handler {}.", entity);
        entitiesWithInputHandlers.removeValue(entity);
    }

    public enum Action {
        //MOVEMENT
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_UP,
        MOVE_DOWN,
        STOP,

        //GLOBAL GAME ACTIONS
        QUIT_GAME
    }
}
