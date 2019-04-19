package com.sad.function.components;

import com.badlogic.ashley.core.Entity;
import com.sad.function.command.Command;
import com.sad.function.command.MoveLeft;
import com.sad.function.command.NullCommand;
import com.sad.function.input.MappedInput;
import com.sad.function.input.definitions.InputConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerInputHandler {
    private static final Logger logger = LogManager.getLogger(PlayerInputHandler.class);

    private Command moveLeft = new MoveLeft();
    private Command moveRight = new NullCommand();
    private Command moveUp = new NullCommand();
    private Command moveDown = new NullCommand();


    public void handleInput(Entity entity, MappedInput mappedInput) {
        //Don't forget to chomp data out of the mapped input.

        //Movement!
        if(mappedInput.isPressed(InputConstants.State.PLAYER_LEFT)) {
            moveLeft.execute(entity);
            mappedInput.eatState(InputConstants.State.PLAYER_LEFT);
        }

        if(mappedInput.isPressed(InputConstants.State.PLAYER_DOWN)) {
            moveDown.execute(entity);
            mappedInput.eatState(InputConstants.State.PLAYER_DOWN);
        }

        if(mappedInput.isPressed(InputConstants.State.PLAYER_RIGHT)) {
            moveRight.execute(entity);
            mappedInput.eatState(InputConstants.State.PLAYER_RIGHT);
        }

        if(mappedInput.isPressed(InputConstants.State.PLAYER_UP)) {
            moveUp.execute(entity);
            mappedInput.eatState(InputConstants.State.PLAYER_UP);
        }
    }
}
