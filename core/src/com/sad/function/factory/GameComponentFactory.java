package com.sad.function.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * TODO this might be useless optimization at this point.
 */
public class GameComponentFactory {
    private static final Logger logger = LogManager.getLogger(GameComponentFactory.class);

    private static HashMap<Class<?>, ComponentFactory> factories;

    public GameComponentFactory() {
        factories = new HashMap<>();
    }

    //Create a less naive implementation of the components.

    /**
     * Register a new factory for creating Components.
     * @param type
     * @param factory
     * @param <T>
     */
    public <T> void registerFactory(Class<T> type, ComponentFactory factory) {
        if (factories.containsKey(type)) {
            logger.warn("GameComponentFactory already has a factory for {}. Overriding it.", type);
        }

        factories.put(type, factory);
    }

    /**
     * Retrieve a factory.
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getFactory(Class<T> type) {
        if(factories.containsKey(type)) {
            ComponentFactory target = factories.get(type);
            if(target != null) {
                return type.cast(factories.get(type));
            }
        }

        logger.error("Try registering a factory for {} types.", type);
        return null;
    }

}
