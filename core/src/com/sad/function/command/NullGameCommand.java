package com.sad.function.command;

import com.badlogic.ashley.core.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NullGameCommand implements GameCommand {
    private static final Logger logger = LogManager.getLogger(NullGameCommand.class);

    @Override
    public void execute(Entity entity, float delta) {
        //Do nothing. We're a null object.
        logger.info("Null command triggered for entity: {}", entity);
    }
}
