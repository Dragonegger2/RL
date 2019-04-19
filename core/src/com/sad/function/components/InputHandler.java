package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.sad.function.command.Command;
import com.sad.function.command.MoveLeft;
import com.sad.function.command.NullCommand;
import com.sad.function.common.Subject;
import com.sad.function.input.MappedInput;
import com.sad.function.input.definitions.InputConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InputHandler extends Subject implements Component{
    private static final Logger logger = LogManager.getLogger(InputHandler.class);
    private List<InputConstants.Action> actionName = new ArrayList<>();

    private Command moveLeft = new MoveLeft();
    private Command moveRight = new NullCommand();
    private Command moveUp = new NullCommand();
    private Command moveDown = new NullCommand();

    public void handle(MappedInput mappedInput, Entity entity) {
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
            moveDown.execute(entity);
            mappedInput.eatState(InputConstants.State.PLAYER_RIGHT);
        }
        if(mappedInput.isPressed(InputConstants.State.PLAYER_UP)) {
            moveDown.execute(entity);
            mappedInput.eatState(InputConstants.State.PLAYER_UP);
        }
    }
}
