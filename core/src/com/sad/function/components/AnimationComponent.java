package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.sad.function.common.Observer;
import com.sad.function.event.Event;
import com.sad.function.event.EventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnimationComponent implements Observer, Component {
    private static final Logger logger = LogManager.getLogger(AnimationComponent.class);

    public float stateTime = 0.0f;

    @Override
    public void onNotify(Event event) {
        if(event.getEventType() == EventType.WALKING_LEFT) {
            //
            logger.info("Changing animation to walk left.");

            //Clear state time.
            stateTime = 0;
        }
    }
}
