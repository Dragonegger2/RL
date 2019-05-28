package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.IntArray;
import com.sad.function.command.GameCommand;
import com.sad.function.command.NullGameCommand;
import com.sad.function.command.QuitGame;
import com.sad.function.command.movement.*;
import com.sad.function.common.DefaultMap;
import com.sad.function.components.Animation;
import com.sad.function.components.Input;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class InputSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(InputSystem.class);

    private ComponentMapper<Animation> mAnimation;

    private IntArray entitiesWithInputHandlers;
    private Map<Action, GameCommand> actions;

    public InputSystem() {
        super(Aspect.all(Input.class));

        entitiesWithInputHandlers = new IntArray();
        actions = new DefaultMap<>(new NullGameCommand());

        float velocity = 2f;
        actions.put(Action.MOVE_LEFT, new MoveLeft(velocity));
        actions.put(Action.MOVE_RIGHT, new MoveRight(velocity));
        actions.put(Action.MOVE_UP, new MoveUp(velocity));
        actions.put(Action.MOVE_DOWN, new MoveDown(velocity));
        actions.put(Action.STOP, new Stop());
        actions.put(Action.QUIT_GAME, new QuitGame());
    }

    @Override
    protected void processSystem() {
        for (int entity : entitiesWithInputHandlers.items) {
            processEntity(entity);
        }
    }

    private void processEntity(int entity) {
        boolean moving = false;
        //TODO push actions into a queue or something so they're not happening willy-nilly.
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            actions.get(Action.MOVE_LEFT).execute(world, entity);
            world.getMapper(Animation.class).create(entity).animationName = "hero-male-side-walk";
            world.getMapper(Animation.class).create(entity).direction = Animation.Direction.LEFT;

            moving = true;
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            actions.get(Action.MOVE_RIGHT).execute(world, entity);
            world.getMapper(Animation.class).create(entity).animationName = "hero-male-side-walk";
            world.getMapper(Animation.class).create(entity).direction = Animation.Direction.RIGHT;

            moving = true;

        }

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            actions.get(Action.MOVE_UP).execute(world, entity);
            world.getMapper(Animation.class).create(entity).animationName = "hero-male-back-walk";
            world.getMapper(Animation.class).create(entity).direction = Animation.Direction.UP;

            moving = true;
        }

        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            actions.get(Action.MOVE_DOWN).execute(world, entity);
            world.getMapper(Animation.class).create(entity).animationName = "hero-male-front-walk";
            world.getMapper(Animation.class).create(entity).direction = Animation.Direction.DOWN;
            moving = true;
        }

        if(!moving){
            switch (mAnimation.create(entity).direction) {
                case UP:
                    world.getMapper(Animation.class).create(entity).animationName = "hero-male-back-idle";
                    break;
                case DOWN:
                    world.getMapper(Animation.class).create(entity).animationName = "hero-male-front-idle";
                    break;
                case LEFT:
                case RIGHT:
                    world.getMapper(Animation.class).create(entity).animationName = "hero-male-side-idle";
                    break;
            }
            actions.get(Action.STOP).execute(world, entity);
        }

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            actions.get(Action.QUIT_GAME).execute(world, entity);
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
