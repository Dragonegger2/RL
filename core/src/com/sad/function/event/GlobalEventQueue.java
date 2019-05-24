package com.sad.function.event;

import com.sad.function.common.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * GameInfo event queue.
 */
public class GlobalEventQueue implements Observer {
    private static final Logger logger = LogManager.getLogger(GlobalEventQueue.class);

    private HashMap<EventType, List<Observer>> map;

    public GlobalEventQueue() {
        map = new HashMap<>();
    }

    @Override
    public void onNotify(Event event) {
        if(map.containsKey(event.getEventType())) {
            map.get(event.getEventType()).forEach(listener -> listener.onNotify(event));

        } else {
            logger.info("No listeners registered for: {}", event.getEventType());
        }

    }

    /**
     * Register a listener by collisionCategory.
     * @param type of event to listen for.
     * @param listener to pass event to when event gets fired.
     */
    public void addListenerByEvent(EventType type, Observer listener) {
        if(!map.containsKey(type)) {
            map.put(type, new ArrayList<>());
        }

        map.get(type).add(listener);

        logger.info("Added new listener {} added for {}", listener, type);
    }
}
