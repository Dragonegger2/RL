package com.sad.function.input;

import com.sad.function.command.GameCommand;
import com.sad.function.command.NullGameCommand;
import com.sad.function.command.QuitGame;
import com.sad.function.command.movement.*;
import com.sad.function.common.DefaultMap;
import com.sad.function.system.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class KeyActionBindings {
    private static final Logger logger = LogManager.getLogger(KeyActionBindings.class);

    public static final Map<Action, GameCommand> actions = bindActions();

    private static Map<Action, GameCommand> bindActions() {
        Map<Action, GameCommand> actions = new DefaultMap<>(new NullGameCommand());

        actions.put(Action.MOVE_LEFT, new MoveLeft());
        actions.put(Action.MOVE_RIGHT, new MoveRight());
        actions.put(Action.MOVE_UP, new MoveUp());
        actions.put(Action.STOP, new Stop());
        actions.put(Action.QUIT_GAME, new QuitGame());

        return actions;
    }
}
