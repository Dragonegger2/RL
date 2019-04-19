package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.sad.function.command.Command;
import com.sad.function.command.MoveLeft;
import com.sad.function.command.NullCommand;
import com.sad.function.common.Observer;
import com.sad.function.common.Subject;
import com.sad.function.input.MappedInput;
import com.sad.function.input.definitions.InputConstants;
import com.sad.function.input.definitions.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class InputHandler implements Component {
      public abstract void handleInput(Entity entity, MappedInput mappedInput);
}
