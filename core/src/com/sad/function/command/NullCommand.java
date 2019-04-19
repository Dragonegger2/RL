package com.sad.function.command;

import com.badlogic.ashley.core.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NullCommand implements Command {
    private static final Logger logger = LogManager.getLogger(NullCommand.class);

    @Override
    public void execute(Entity entity) {
        //Do nothing. We're a null object.
        logger.info("Null command triggered for entity: {}", entity);
    }
}
