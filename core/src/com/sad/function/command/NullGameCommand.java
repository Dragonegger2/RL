package com.sad.function.command;

import com.artemis.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NullGameCommand implements GameCommand {
    private static final Logger logger = LogManager.getLogger(NullGameCommand.class);

    @Override
    public void execute(World world, int entity) {
        logger.info("Null command triggered for entity: {}", entity);
    }
}
